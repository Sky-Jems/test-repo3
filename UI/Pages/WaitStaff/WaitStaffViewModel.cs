using System;
using System.Linq;
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

public class WaitStaffViewModel : ReactiveObject, IScreen
{
    public RoutingState Router { get; } = new ();

    private readonly ICartService _cartService;
    private readonly IOrderService _orderService;

    public OrderCartPanelViewModel OrderCartPanelViewModel { get; set; } = new ();
    public ReactiveCommand<Unit, Unit> SummaryButtonCommand { get; }
    public event EventHandler<NotificationEventArgs> TriggerNotif;

    public WaitStaffViewModel()
    {
        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        _orderService = ServiceLocator.Services.GetRequiredService<IOrderService>();
        
        Router.Navigate.Execute(new CategoriesViewModel(this));
        SummaryButtonCommand = ReactiveCommand.CreateFromTask(PayOrder);

        OrderCartPanelViewModel.CartItemClicked += HandleCartItemClicked;
    }
    
    private void HandleCartItemClicked(LineItem lineItem)
    {
        var currentMenuVm = Router.NavigationStack.LastOrDefault() as MenuViewModel;

        if (currentMenuVm is null)
        {
            var newMenuVm = new MenuViewModel(this, lineItem.Category);
            Router.Navigate.Execute(newMenuVm).Subscribe();
        }
        else
        {
            currentMenuVm.UpdateCategory(lineItem.Category);
        }
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
