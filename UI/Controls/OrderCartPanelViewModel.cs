using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Reactive.Linq;
using System.Threading.Tasks;
using AvaloniaDialogs.Views;
using DynamicData.Binding;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using Pos.Dialogs;
using pos.Extensions;
using Pos.Models;
using pos.Models.EventArgs;
using ReactiveUI;

namespace Pos.Controls;

public class OrderCartPanelViewModel : ReactiveObject
{
    private readonly ICartService _cartService;
    private readonly IOrderService _orderService;
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
    public string Customer
    {
        get => _cartService.Customer;
        set => _cartService.Customer = value;
    }
    private readonly ObservableAsPropertyHelper<bool> _canPay;
    public bool CanPay => _canPay.Value;
    public bool CanModifyItems => _cartService.CanModifyItems;
    public event Action<LineItem>? CartItemClicked;
    public ReactiveCommand<LineItem, Unit> NavigateToMenuCommand { get; }
    public ReactiveCommand<LineItem, Unit> ClickPlusCommand { get; }
    public ReactiveCommand<LineItem, Unit> ClickMinusCommand { get; }
    public ReactiveCommand<LineItem, Unit> RemoveLineItemCommand { get; }
    public ReactiveCommand<Unit, Unit> ClearLineItemsCommand { get; }
    public ReactiveCommand<Unit, Unit> PayLaterCommand { get; }
    public ReactiveCommand<Unit, Unit> UpdateOrderCommand { get; }
    private ReactiveCommand<Unit, Unit> LoadOrderToCartCommand { get; }
    public ReactiveCommand<Unit, Unit> PayOrderCommand { get; }
    public event EventHandler<NotificationEventArgs>? TriggerNotif;

    public OrderCartPanelViewModel()
    {
        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        _orderService = ServiceLocator.Services.GetRequiredService<IOrderService>();

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

        NavigateToMenuCommand = ReactiveCommand.Create<LineItem>(HandleClickLineItem);
        ClickPlusCommand = ReactiveCommand.CreateFromTask<LineItem>(HandleClickPlusAsync);
        ClickMinusCommand = ReactiveCommand.CreateFromTask<LineItem>(HandleClickMinusAsync);
        RemoveLineItemCommand = ReactiveCommand.CreateFromTask<LineItem>(RemoveLineItemAsync);
        ClearLineItemsCommand = ReactiveCommand.CreateFromTask(ClearLineItemsAsync);
        PayLaterCommand = ReactiveCommand.Create(PayLater);
        UpdateOrderCommand = ReactiveCommand.CreateFromTask(SaveCustomerAsync);
        LoadOrderToCartCommand = ReactiveCommand.CreateFromTask(LoadOrderToCartAsync);
        PayOrderCommand = ReactiveCommand.Create(PayOrder);
    }

    private async Task LoadOrderToCartAsync()
    {
        try
        {
            var order = await _orderService.GetOrderTransaction(OrderTransactionId);
            _cartService.LoadOrder(order);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
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
        _cartService.Items.Remove(lineItem);
        await _orderService.RemoveLineItem(lineItem.Id);
    }

    private async Task HandleClickPlusAsync(LineItem item)
    {
        if (!_cartService.CanModifyItems)
        {
            var lockedDialog = new SingleActionDialog
            {
                Message = "Items cannot be modified because the order is already completed or partially paid.",
                ButtonText = "OK"
            };

            await lockedDialog.ShowAsync();
            return;
        }

        _cartService.AddItem(item);

        if (_cartService.OrderId is null)
            return;

        var updatedOrder = await _orderService.UpdateLineItem(LineItemMapper.ToDto(item, _cartService.OrderId));
        _cartService.LoadOrder(updatedOrder);

        _cartService.SelectedItem = _cartService.Items
            .FirstOrDefault(x => x.ProductId == item.ProductId);
    }

    private async Task HandleClickMinusAsync(LineItem item)
    {
        if (!_cartService.CanModifyItems)
        {
            var lockedDialog = new SingleActionDialog
            {
                Message = "Items cannot be modified because the order is already completed or partially paid.",
                ButtonText = "OK"
            };

            await lockedDialog.ShowAsync();
            return;
        }

        var previousQuantity = item.Quantity;
        _cartService.RemoveItem(item);

        if (_cartService.OrderId is null)
            return;

        var updatedOrder = previousQuantity == 1
            ? await _orderService.RemoveLineItem(item.Id)
        : await _orderService.UpdateLineItem(LineItemMapper.ToDto(item, _cartService.OrderId));

        _cartService.LoadOrder(updatedOrder);

        if (previousQuantity > 1)
        {
            _cartService.SelectedItem = _cartService.Items
                .FirstOrDefault(x => x.ProductId == item.ProductId);
        }
    }

    private async Task ClearLineItemsAsync()
    {
        if (_cartService.OrderId is null) return;

        _cartService.Items.Clear();
        await _orderService.ClearLineItems(_cartService.OrderId.Value);
    }

    private void PayLater()
    {
        _cartService.ResetOrder();
    }

    private async Task SaveCustomerAsync()
    {
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

    private async void PayOrder()
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
    }
}
