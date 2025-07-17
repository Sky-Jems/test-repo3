using System;
using System.Collections.ObjectModel;
using System.Reactive;
using System.Reactive.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using Pos.Models;
using pos.Models.EventArgs;
using Pos.Util;
using ReactiveUI;

namespace Pos.Dialogs;

public class PaymentMethodDialogViewModel : ReactiveObject
{
    public readonly ICartService _cartService;
    private readonly IOrderService _orderService;
    public ObservableCollection<Payment> Payments => _cartService.Payments;
    public ReactiveCommand<Unit, Unit> CompletePaymentCommand { get; }
    public ReactiveCommand<Unit, Unit> AddPaymentCommand { get; }
    public string Customer => _cartService.Customer;
    public long ItemsCount => _cartService.Items.Count;
    public decimal SubTotal => _cartService.SubTotal;
    public decimal DiscountAmount => _cartService.DiscountAmount;
    public decimal Total => _cartService.Total;
    private readonly ObservableAsPropertyHelper<decimal> _amountPaid;
    public decimal AmountPaid => _amountPaid.Value;
    private readonly ObservableAsPropertyHelper<decimal> _remainingBalance;
    public bool CanAddPayment => _cartService.PaymentStatus == PaymentStatus.PENDING;
    public bool CanCompletePayment => _cartService.PaymentStatus == PaymentStatus.COMPLETED;
    public decimal RemainingBalance => _remainingBalance.Value;
    public string PaymentMethod
    {
        get => _cartService.PaymentMethod;
        set => _cartService.PaymentMethod = value;
    }
    private string _amountText;
    public string AmountText
    {
        get => _amountText;
        set
        {
            this.RaiseAndSetIfChanged(ref _amountText, value);

            if (decimal.TryParse(value, out var parsed))
            {
                _cartService.Amount = parsed;
            }
            else
            {
                // Optionally, set a default or leave it unchanged
                _cartService.Amount = 0;
            }
        }
    }
    public string Notes
    {
        get => _cartService.Notes;
        set
        {
            if (_cartService.Notes != value)
            {
                _cartService.Notes = value;
                this.RaisePropertyChanged();
            }
        }
    }
    public event EventHandler<NotificationEventArgs>? TriggerNotif;
    public event Action? RequestClose;
    private readonly ObservableAsPropertyHelper<string?> _notePrefix;
    public string? NotePrefix => _notePrefix.Value;

    public PaymentMethodDialogViewModel()
    {
        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        _orderService = ServiceLocator.Services.GetRequiredService<IOrderService>();

        CompletePaymentCommand = ReactiveCommand.Create(CompletePaymentAsync);
        AddPaymentCommand = ReactiveCommand.CreateFromTask(AddPaymentAsync);

        _cartService
            .WhenAnyValue(x => x.AmountPaid)
            .ObserveOn(RxApp.MainThreadScheduler)
            .ToProperty(this, x => x.AmountPaid, out _amountPaid);

        _cartService
            .WhenAnyValue(x => x.RemainingBalance)
            .ObserveOn(RxApp.MainThreadScheduler)
            .ToProperty(this, x => x.RemainingBalance, out _remainingBalance);
        
        _cartService
            .WhenAnyValue(x => x.PaymentStatus)
            .ObserveOn(RxApp.MainThreadScheduler)
            .Subscribe(_ =>
            {
                this.RaisePropertyChanged(nameof(CanAddPayment));
                this.RaisePropertyChanged(nameof(CanCompletePayment));
            });

        _cartService.Payments.CollectionChanged += (_, _) =>
        {
            this.RaisePropertyChanged(nameof(AmountPaid));
            this.RaisePropertyChanged(nameof(RemainingBalance));
        };

        _cartService.WhenAnyValue(x => x.PaymentMethod)
            .Select(method => string.Equals(method, "cash", StringComparison.OrdinalIgnoreCase) ? null : "*")
            .ToProperty(this, x => x.NotePrefix, out _notePrefix);

        AddPaymentCommand.ThrownExceptions
            .Subscribe(ex =>
            {
                Console.Error.WriteLine($"[Command Error] {ex}");

                TriggerNotif?.Invoke(this, new NotificationEventArgs
                {
                    Message = "Failed to make payment.",
                    NotifType = Constants.NotifType.Error
                });
            });
    }

    private void CompletePaymentAsync()
    {
        if (_cartService.Payments.Count == 0)
        {
            TriggerNotif?.Invoke(this, new NotificationEventArgs
            {
                Message = "Please select a payment method and enter an amount to add a payment.",
                NotifType = Constants.NotifType.Error
            });
            return;
        }

        if (_cartService.RemainingBalance <= 0)
        {
            _cartService.ResetPayments();
        }
        RequestClose?.Invoke();
    }

    private async Task AddPaymentAsync()
    {
        string? validationError = ValidatePaymentAmount();
        if (validationError is not null)
        {
            TriggerNotif?.Invoke(this, new NotificationEventArgs
            {
                Message = validationError,
                NotifType = Constants.NotifType.Error
            });
            return;
        }

        Payment payment = _cartService.MakePayment();
        PaymentResponseDto paymentResponse = await _orderService.PayOrder(payment);
        _cartService.AddPayment(payment);
        _cartService.ApplyPayment(paymentResponse);

        PaymentMethod = PaymentMethod;
        this.RaisePropertyChanged(nameof(PaymentMethod));

        var message = $"Order has been paid with {payment.PaymentMethod} for ₱{payment.Amount:N2}.";
        if (paymentResponse.OrderId == _cartService.OrderId && paymentResponse.RemainingAmount <= 0)
        {
            message = $"Order has been fully paid with {payment.PaymentMethod} and moved to Completed Orders.";
            _cartService.PaymentStatus = PaymentStatus.COMPLETED;
        }

        TriggerNotif?.Invoke(this, new NotificationEventArgs
        {
            Message = message,
            NotifType = Constants.NotifType.Success
        });
        AmountText = "";
        Notes = "";
    }

    private string? ValidatePaymentAmount()
    {
        if (_cartService.OrderId is null || _cartService.PaymentStatus == PaymentStatus.COMPLETED)
            return "Order has already been completed.";

        if (string.IsNullOrWhiteSpace(AmountText))
            return "Amount is required.";

        if (Regex.IsMatch(AmountText, @"[a-zA-Z]"))
            return "Amount must contain only numbers and optionally a decimal point.";

        if (!decimal.TryParse(AmountText, out var parsedAmount))
            return "Invalid amount format.";

        if (parsedAmount < 0)
            return "Amount must be a positive value or zero.";

        if (parsedAmount > _cartService.RemainingBalance)
            return $"Amount exceeds the remaining balance of ₱{_cartService.RemainingBalance:N2}.";

        if (PaymentMethod != "cash" && String.IsNullOrWhiteSpace(_cartService.Notes))
        {
            return "Reference Number cannot be empty.";
        }

        return null;
    }
}
