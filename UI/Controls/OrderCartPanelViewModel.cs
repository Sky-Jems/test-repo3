using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Reactive.Linq;
using System.Threading.Tasks;
using DynamicData.Binding;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using Pos.Models;
using ReactiveUI;

namespace Pos.Controls;

public class OrderCartPanelViewModel : ReactiveObject
{
    private readonly ICartService _cartService;
    private readonly IOrderService _orderService;
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
    public ReactiveCommand<LineItem, Unit> RemoveLineItemCommand { get; }
    public ReactiveCommand<Unit, Unit> ClearLineItemsCommand { get; }
    public ReactiveCommand<Unit, Unit> PayLaterCommand { get; }

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
        RemoveLineItemCommand = ReactiveCommand.CreateFromTask<LineItem>(RemoveLineItemAsync);
        ClearLineItemsCommand = ReactiveCommand.CreateFromTask(ClearLineItemsAsync);
        PayLaterCommand = ReactiveCommand.Create(PayLater);
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

    private async Task ClearLineItemsAsync()
    {
        _cartService.Items.Clear();
        await _orderService.ClearLineItems(_cartService.OrderId.Value);
    }

    private void PayLater()
    {
       _cartService.ResetOrder();
    }
}
