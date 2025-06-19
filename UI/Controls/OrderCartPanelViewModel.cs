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
    public ObservableCollection<LineItem> OrderList { get; set; } = [];
    private decimal _cartTotal;
    public decimal CartTotal
    {
        get => _cartTotal;
        set => this.RaiseAndSetIfChanged(ref _cartTotal, value);
    }
    private readonly ICartService _cartService;
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


    public OrderCartPanelViewModel()
    {
        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        OrderList = _cartService.Items;
        
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
    }
}
