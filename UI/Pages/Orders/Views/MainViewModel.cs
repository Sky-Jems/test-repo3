using System;
using System.Reactive;
using System.Threading.Tasks;
using Avalonia.Data;
using pos.Api;
using Pos.Controls;
using Pos.Models;
using ReactiveUI;
using pos.Extensions;
using System.Collections.Generic;
using System.Linq;
using static Pos.Util.Constants;
using System.Web;
using Microsoft.Extensions.DependencyInjection;
using pos.Models.EventArgs;
using Pos.Util;

namespace Pos.Pages.Orders;

public partial class MainViewModel : ReactiveObject, IRoutableViewModel
{
    public string? UrlPathSegment => throw new NotImplementedException();
    public IScreen HostScreen { get; }

    private readonly IOrderTransactionService _orderTransactionService;

    private string _Status;
    public string Status
    {
        get => _Status;
        set => this.RaiseAndSetIfChanged(ref _Status, value);
    }

    public event EventHandler<NotificationEventArgs>? TriggerNotif;

    private DateTimeOffset? _StartDate;
    public DateTimeOffset? StartDate
    {
        get => _StartDate;
        set
        {
            if (!value.HasValue)
                return;

            _StartDate = value.Value.DayStart();
            this.RaisePropertyChanged(nameof(StartDate));

            IsDateFilterable = false;
            if (_StartDate > EndDate)
                TriggerNotif?.Invoke(this, new NotificationEventArgs
                    {
                        Message = "Start date cannot be late than end date",
                        NotifType = Constants.NotifType.Error
                    });
            // If you want to throw an exception instead of triggering a notification, uncomment the line below
                // throw new DataValidationException("Start date cannot be late than end date");
            else if (EndDate != null)
                IsDateFilterable = true;

            this.RaisePropertyChanged(nameof(EndDate));
        }
    }

    private DateTimeOffset? _EndDate;
    public DateTimeOffset? EndDate
    {
        get => _EndDate;
        set
        {
            if (!value.HasValue)
                return;

            _EndDate = value.Value.DayEnd();
            this.RaisePropertyChanged(nameof(EndDate));

            IsDateFilterable = false;
            if (_EndDate < StartDate)
                throw new DataValidationException("End date cannot be early than start date");

            IsDateFilterable = true;
            this.RaisePropertyChanged(nameof(StartDate));
        }
    }

    private bool _IsDateFilterable;
    public bool IsDateFilterable
    {
        get => _IsDateFilterable;
        set => this.RaiseAndSetIfChanged(ref _IsDateFilterable, value);
    }

    private List<OrderTransaction> _OrderTransactions = [];
    public List<OrderTransaction> OrderTransactions
    {
        get => _OrderTransactions;
        set => this.RaiseAndSetIfChanged(ref _OrderTransactions, value);
    }

    private decimal _AmountOfSales;
    public decimal AmountOfSales
    {
        get => _AmountOfSales;
        set => this.RaiseAndSetIfChanged(ref _AmountOfSales, value);
    }

    private bool _ShowOrderCartPanelButton;
    public bool ShowOrderCartPanelButton
    {
        get => _ShowOrderCartPanelButton;
        set => this.RaiseAndSetIfChanged(ref _ShowOrderCartPanelButton, value);
    }

    public OrderDisplayPanelViewModel OrderDisplayPanelViewModel { get; set; } = new OrderDisplayPanelViewModel();
    public ReactiveCommand<Unit, Unit> AddOrdersButtonCommand { get; }
    public ReactiveCommand<Unit, Unit> FilteredOrderTransactionsCommand { get; }

    public MainViewModel(IScreen screen, OrderTransactionService orderTransactionService, string status)
    {
        Status = status;
        _ShowOrderCartPanelButton = status == OrderStatusType.PENDING.ToString();
        HostScreen = screen;
        _orderTransactionService = orderTransactionService;
        AddOrdersButtonCommand = ReactiveCommand.Create(() =>
        {
            PopulateOrderCartPanel(OrderDisplayPanelViewModel.OrderTransactionDetails.Id);
            MessageBus.Current.SendMessage(new SelectedTabIndexMessage(0));
        });
        FilteredOrderTransactionsCommand = ReactiveCommand.CreateFromTask(LoadFilteredOrderTransactionsAsync);
    }

    private async Task LoadFilteredOrderTransactionsAsync()
    {
        try
        {
            if (StartDate.HasValue && EndDate.HasValue)
            {
                OrderTransactions = [];
                string start = HttpUtility.UrlEncode(StartDate.Value.ToISO8601());
                string end = HttpUtility.UrlEncode(EndDate.Value.ToISO8601());
                OrderTransactions = (await _orderTransactionService.GetFilteredOrderTransactionsByStatus(Status, start, end))
                    .OrderByDescending(transaction => transaction.Order.CreatedAt).ToList();
                AmountOfSales = OrderTransactions.Sum(transaction => transaction.NetAmount);
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
        }
    }

    public void PopulateOrderCartPanel(long orderTransactionId)
    {
        var orderCartPanelViewModel = ServiceLocator.Services.GetRequiredService<OrderCartPanelViewModel>();
        orderCartPanelViewModel.LoadOrderToCart(orderTransactionId);
    }
}