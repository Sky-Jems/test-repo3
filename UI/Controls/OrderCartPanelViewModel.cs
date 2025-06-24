using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Reactive.Linq;
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
    private readonly ObservableAsPropertyHelper<bool> _canPay;
    public bool CanPay => _canPay.Value;
    public event Action<LineItem>? CartItemClicked;
    public ReactiveCommand<LineItem, Unit> NavigateToMenuCommand { get; }

    public OrderCartPanelViewModel()
    {
        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        
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
            .Select(_ => _cartService.PaymentStatus == PaymentStatus.Pending && _cartService.Items.Any())
            .ToProperty(this, x => x.CanPay, out _canPay);

        NavigateToMenuCommand = ReactiveCommand.Create<LineItem>(HandleClickLineItem);
    }

    private void HandleClickLineItem(LineItem lineItem)
    {
        CartItemClicked?.Invoke(lineItem);
    }
}
