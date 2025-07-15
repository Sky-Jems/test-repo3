using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Reactive.Linq;
using System.Threading.Tasks;
using System.Web;
using AvaloniaDialogs.Views;
using DynamicData.Binding;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using Pos.Dialogs;
using pos.Extensions;
using Pos.Models;
using pos.Models.EventArgs;
using Pos.Util;
using ReactiveUI;
using ReactiveUI.Fody.Helpers;

namespace Pos.Controls;

public class OrderCartPanelViewModel : ReactiveObject
{
    private readonly ICartService _cartService;
    private readonly IOrderService _orderService;
    private readonly IOrderTransactionService _orderTransactionService;
    private readonly IUIInteractionService _uiInteractionService;
    private long OrderTransactionId { get; set; }
    public ObservableCollection<LineItem> OrderList => _cartService.Items;
    private readonly ObservableAsPropertyHelper<decimal> _cartTotal;
    public decimal CartTotal => _cartTotal.Value;
    private LineItem _selectedItem;
    public LineItem SelectedItem
    {
        get => _selectedItem;
        set
        {
            this.RaiseAndSetIfChanged(ref _selectedItem, value);
            _cartService.SelectedItem = value; // sync to service
        }
    }
    [Reactive] public string? Customer { get; set; }
    private readonly ObservableAsPropertyHelper<bool> _showItems;
    private readonly ObservableAsPropertyHelper<bool> _canPay;
    private bool CanPay => _canPay.Value;
    public bool CanShowItems => _showItems.Value;
    public bool CanModifyItems => _cartService.CanModifyItems;
    public bool IsCompleted => _cartService.PaymentStatus != PaymentStatus.PENDING;
    public event Action<LineItem>? CartItemClicked;
    public event Action NavigateToCategory;
    public ReactiveCommand<LineItem, Unit> NavigateToMenuCommand { get; }
    public ReactiveCommand<LineItem, Unit> ClickPlusCommand { get; }
    public ReactiveCommand<LineItem, Unit> ClickMinusCommand { get; }
    public ReactiveCommand<LineItem, Unit> RemoveLineItemCommand { get; }
    public ReactiveCommand<Unit, Unit> ClearLineItemsCommand { get; }
    public ReactiveCommand<Unit, Unit> PayLaterCommand { get; }
    public ReactiveCommand<Unit, Unit> UpdateOrderCommand { get; }
    private ReactiveCommand<Unit, Unit> LoadOrderToCartCommand { get; }
    public ReactiveCommand<Unit, Unit> PayOrderCommand { get; }
    public ReactiveCommand<Unit, Unit> NewOrderCommand { get; }
    public ReactiveCommand<Unit, bool> ValidateCustomerNameCommand { get; }
    public event EventHandler<NotificationEventArgs>? TriggerNotif;
    private bool _isModifyingOrder;
    private string? _originalCustomer;
    [Reactive] public bool ShouldFocusCustomer { get; set; }

    public OrderCartPanelViewModel()
    {
        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        _orderService = ServiceLocator.Services.GetRequiredService<IOrderService>();
        _orderTransactionService = ServiceLocator.Services.GetRequiredService<IOrderTransactionService>();
        _uiInteractionService = ServiceLocator.Services.GetRequiredService<IUIInteractionService>();

        _cartService
            .WhenAnyValue(x => x.Total)
            .ObserveOn(RxApp.MainThreadScheduler)
            .ToProperty(this, x => x.CartTotal, out _cartTotal);

        // Sync from service to viewmodel
        _cartService.WhenAnyValue(x => x.SelectedItem)
            .BindTo(this, x => x.SelectedItem);

        // Notify CanPay changes on PaymentStatus or Items changes
        var paymentStatusChanged = _cartService.WhenAnyValue(x => x.PaymentStatus).Select(_ => Unit.Default);
        var itemsChanged = _cartService.Items
            .ToObservableChangeSet()
            .Select(_ => Unit.Default);

        paymentStatusChanged
            .Merge(itemsChanged)
            .Select(_ => _cartService.PaymentStatus == PaymentStatus.PENDING && _cartService.Items.Any())
            .ToProperty(this, x => x.CanPay, out _canPay);

        _cartService.WhenAnyValue(x => x.Customer)
            .Subscribe(_ => this.RaisePropertyChanged(nameof(Customer)));

        _cartService.WhenAnyValue(x => x.PaymentStatus)
                .Select(status => status == PaymentStatus.PENDING)
                .Do(_ => this.RaisePropertyChanged(nameof(IsCompleted)))
                .ToProperty(this, x => x.CanShowItems, out _showItems);
        
        _cartService.WhenAnyValue(x => x.Customer)
            .Subscribe(customer =>
            {
                _originalCustomer = customer;
                Customer = customer;
            });
        
        _uiInteractionService.RequestFocus += field =>
        {
            if (field == "Customer")
                ShouldFocusCustomer = true;
        };

        NavigateToMenuCommand = ReactiveCommand.Create<LineItem>(HandleClickLineItem);
        ClickPlusCommand = ReactiveCommand.CreateFromTask<LineItem>(HandleClickPlusAsync);
        ClickMinusCommand = ReactiveCommand.CreateFromTask<LineItem>(HandleClickMinusAsync);
        RemoveLineItemCommand = ReactiveCommand.CreateFromTask<LineItem>(RemoveLineItemAsync);
        ClearLineItemsCommand = ReactiveCommand.CreateFromTask(ClearLineItemsAsync);
        PayLaterCommand = ReactiveCommand.Create(PayLater);
        UpdateOrderCommand = ReactiveCommand.CreateFromTask(SaveCustomerAsync);
        LoadOrderToCartCommand = ReactiveCommand.CreateFromTask(LoadOrderToCartAsync);
        PayOrderCommand = ReactiveCommand.CreateFromTask(PayOrder);
        NewOrderCommand = ReactiveCommand.Create(StartNewOrder);
        ValidateCustomerNameCommand = ReactiveCommand.CreateFromTask(ValidateCustomerNameAsync);

        ClickPlusCommand.ThrownExceptions
            .Subscribe(ex => HandleCommandError("Failed to increase quantity.", ex));
        ClickMinusCommand.ThrownExceptions
            .Subscribe(ex => HandleCommandError("Failed to decrease quantity.", ex));
        RemoveLineItemCommand.ThrownExceptions
            .Subscribe(ex => HandleCommandError("Failed to remove item.", ex));
        ClearLineItemsCommand.ThrownExceptions
            .Subscribe(ex => HandleCommandError("Failed to clear items.", ex));
        UpdateOrderCommand.ThrownExceptions
            .Subscribe(ex => HandleCommandError("Failed to save customer information.", ex));
        LoadOrderToCartCommand.ThrownExceptions
            .Subscribe(ex => HandleCommandError("Failed to load order to cart.", ex));
        PayOrderCommand.ThrownExceptions
            .Subscribe(ex => HandleCommandError("Failed to open payment dialog.", ex));
        ValidateCustomerNameCommand.ThrownExceptions
            .Subscribe(ex => HandleCommandError("Failed to validate customer name.", ex));
    }

    private async Task LoadOrderToCartAsync()
    {
        var order = await _orderService.GetOrderTransaction(OrderTransactionId);
        _cartService.LoadOrder(order);
    }

    public void LoadOrderToCart(long orderTransactionId)
    {
        OrderTransactionId = orderTransactionId;
        LoadOrderToCartCommand.Execute().Subscribe();
    }

    private void HandleClickLineItem(LineItem lineItem)
    {
        CartItemClicked?.Invoke(lineItem);
    }

    private async Task RemoveLineItemAsync(LineItem lineItem)
    {
        if (!CheckCanModify())
            return;

        var updatedOrder = await _orderService.RemoveLineItem(lineItem.Id);
        _cartService.LoadOrder(updatedOrder);
    }

    private async Task HandleClickPlusAsync(LineItem item)
    {
        if (!CheckCanModify())
            return;

        if (_isModifyingOrder || _cartService.OrderId is null)
            return;

        try
        {
            _isModifyingOrder = true;
            var lineItemDto = LineItemMapper.ToDto(item, _cartService.OrderId);
            lineItemDto.Quantity++;
            var updatedOrder = await _orderService.UpdateLineItem(lineItemDto);
            _cartService.LoadOrder(updatedOrder);

            _cartService.SelectedItem = _cartService.Items
                .FirstOrDefault(x => x.ProductId == item.ProductId);
        }
        finally
        {
            _isModifyingOrder = false;
        }
    }

    private async Task HandleClickMinusAsync(LineItem item)
    {
        if (!CheckCanModify())
            return;

        if (_isModifyingOrder || _cartService.OrderId is null)
            return;

        try
        {
            _isModifyingOrder = true;
            var previousQuantity = item.Quantity;
            var lineItemDto = LineItemMapper.ToDto(item, _cartService.OrderId);
            lineItemDto.Quantity--;

            var updatedOrder = previousQuantity == 1
                ? await _orderService.RemoveLineItem(item.Id)
                : await _orderService.UpdateLineItem(lineItemDto);

            _cartService.LoadOrder(updatedOrder);

            if (previousQuantity > 1)
            {
                _cartService.SelectedItem = _cartService.Items
                    .FirstOrDefault(x => x.ProductId == item.ProductId);
            }
        }
        finally
        {
            _isModifyingOrder = false;
        }
    }

    private async Task ClearLineItemsAsync()
    {
        if (_cartService.OrderId is null) return;

        var updatedOrder = await _orderService.ClearLineItems(_cartService.OrderId.Value);
        _cartService.LoadOrder(updatedOrder);
    }

    private void PayLater()
    {
        _cartService.ResetOrder();
        TriggerNotif?.Invoke(this, new NotificationEventArgs
        {
            Message = "Order has been moved to Pending Orders.",
            NotifType = Constants.NotifType.Success
        });
        NavigateToCategory.Invoke();
    }

    private async Task<bool> ValidateCustomerNameAsync()
    {
        var startDate = new DateTimeOffset(DateTime.UtcNow).DayStart();
        var endDate = new DateTimeOffset(DateTime.UtcNow).DayEnd();
        string start = HttpUtility.UrlEncode(startDate.ToISO8601());
        string end = HttpUtility.UrlEncode(endDate.ToISO8601());
        var pendingOrders = await _orderTransactionService
            .GetFilteredOrderTransactionsByStatus(Constants.OrderStatusType.PENDING.ToString(), start, end);
        if (string.IsNullOrWhiteSpace(Customer)) 
            return false;

        // Check if any order has the same customer name (case-insensitive)
        var existingCustomer = pendingOrders
            .Any(order => string.Equals(order.Order.Customer?.Trim(), Customer.Trim(), StringComparison.OrdinalIgnoreCase));

        return existingCustomer;
    }
    
    private bool HasCustomerNameChanged()
    {
        return !string.Equals(_originalCustomer?.Trim(), Customer?.Trim(), StringComparison.Ordinal);
    }

    private async Task SaveCustomerAsync()
    {
        if (!HasCustomerNameChanged()) return;

        var isExistingCustomer = await ValidateCustomerNameAsync();
        if (isExistingCustomer)
        {
            TriggerNotif?.Invoke(this, new NotificationEventArgs
            {
                Message = $"A pending order already exists for '{Customer}'. Please enter a different name.",
                NotifType = Constants.NotifType.Error
            });
            
            Customer = _originalCustomer;
            ShouldFocusCustomer = true;
            return;
        }

        if (_cartService.OrderId is null)
        {
            var newOrder = new Order
            {
                Customer = Customer,
                LineItems = []
            };

            var createdOrder = await _orderService.AddOrder(newOrder);
            _cartService.OrderId = createdOrder.OrderId;
            _cartService.Customer = createdOrder.Order.Customer;
            return;
        }

        UpdateOrderDto updateOrderDto = new UpdateOrderDto
        {
            Id = _cartService.OrderId.Value,
            Customer = Customer
        };
        await _orderService.UpdateCustomer(updateOrderDto);
    }

    private async Task PayOrder()
    {
        PaymentMethodDialog dialog = new();
        if (dialog.DataContext is PaymentMethodDialogViewModel vm)
        {
            vm.TriggerNotif -= OnPaymentNotifReceived;
            vm.TriggerNotif += OnPaymentNotifReceived;

            void HandleRequestClose()
            {
                vm.RequestClose -= HandleRequestClose; // unsubscribe once used
                dialog.Close();
            }

            // Subscribe the close handler
            vm.RequestClose += HandleRequestClose;
        }
        await dialog.ShowAsync();
    }

    private void OnPaymentNotifReceived(object? sender, NotificationEventArgs e)
    {
        TriggerNotif?.Invoke(this, e);
        this.RaisePropertyChanged(nameof(IsCompleted));
    }

    private void StartNewOrder()
    {
        _cartService.ResetOrder();
        NavigateToCategory.Invoke();
    }

    private bool CheckCanModify()
    {
        if (_cartService.CanModifyItems)
            return true;

        ShowLockedDialog();
        return false;
    }

    private static async void ShowLockedDialog()
    {
        var lockedDialog = new SingleActionDialog
        {
            Message = "Items cannot be modified because the order is already completed or partially paid.",
            ButtonText = "OK"
        };

        await lockedDialog.ShowAsync();
    }

    private void HandleCommandError(string message, Exception ex)
    {
        Console.Error.WriteLine($"[Command Error] {ex}");

        TriggerNotif?.Invoke(this, new NotificationEventArgs
        {
            Message = message,
            NotifType = Constants.NotifType.Error
        });
    }
}
