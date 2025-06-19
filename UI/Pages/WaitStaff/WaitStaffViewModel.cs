using System;
using System.Collections.ObjectModel;
using System.Reactive;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Models.EventArgs;
using pos.Util;
using Pos.Controls;
using Pos.Dialogs;
using pos.Extensions;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.WaitStaff;

public partial class WaitStaffViewModel : ReactiveObject, IScreen
{
    public string? UrlPathSegment => throw new NotImplementedException();
    public RoutingState Router { get; } = new RoutingState();
    private readonly ICategoryService categoryService;
    private readonly IProductService productService;
    private readonly ICartService _cartService;
    private readonly IOrderService _orderService;
    public ObservableCollection<LineItem> CartItems => _cartService.Items;
    public decimal CartTotal => _cartService.Total;
    public string CustomerName
    {
        get => _cartService.CustomerName;
        set => _cartService.CustomerName = value;
    }

    public OrderCartPanelViewModel OrderCartPanelViewModel { get; set; } = new OrderCartPanelViewModel();
    public ReactiveCommand<Unit, Unit> SummaryButtonCommand { get; }

    public event EventHandler<NotificationEventArgs> TriggerNotif;

    public WaitStaffViewModel(ICategoryService categoryService, IProductService productService)
    {
        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        _cartService.Items.CollectionChanged += (_, _) => this.RaisePropertyChanged(nameof(CartTotal));
        _cartService.WhenAnyValue(x => x.Total)
            .Subscribe(total => OrderCartPanelViewModel.CartTotal = total);

        _cartService.WhenAnyValue(x => x.CustomerName)
            .Subscribe(_ => this.RaisePropertyChanged(nameof(CustomerName)));

        this.categoryService = categoryService;
        this.productService = productService;
        _orderService = ServiceLocator.Services.GetRequiredService<IOrderService>();
        Router.Navigate.Execute(new CategoriesViewModel(this, this.categoryService, this.productService));

        OrderCartPanelViewModel.OrderList = CartItems;
        SummaryButtonCommand = ReactiveCommand.CreateFromTask(PayOrder);
    }

    private async Task PayOrder()
    {
        PaymentMethodDialog dialog = new();
        string selectedPayment = (await dialog.ShowAsync()).GetValueOrDefault();
        if (!string.IsNullOrEmpty(selectedPayment))
        {
            await _orderService.PayOrder(_cartService.OrderId, selectedPayment);
            _cartService.ClearItems();
            _cartService.OrderId = null;
            TriggerNotif?.Invoke(this, new NotificationEventArgs
            {
                Message = $"Order has been paid with {selectedPayment}.",
                NotifType = NotifConstants.NotifType.Success
            });
        }
    }
}
