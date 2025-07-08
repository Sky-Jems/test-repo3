using System;
using System.Linq;
using pos.Models.EventArgs;
using Pos.Controls;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.WaitStaff;

public class WaitStaffViewModel : ReactiveObject, IScreen
{
    public RoutingState Router { get; } = new();

    public OrderCartPanelViewModel OrderCartPanelViewModel { get; set; } = new();
    public event EventHandler<NotificationEventArgs> TriggerNotif;

    public WaitStaffViewModel()
    {
        Router.Navigate.Execute(new CategoriesViewModel(this));

        OrderCartPanelViewModel.CartItemClicked += HandleCartItemClicked;
        OrderCartPanelViewModel.TriggerNotif += (sender, args) =>
        {
            TriggerNotif?.Invoke(this, args);
        };
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
}
