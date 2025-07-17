using System;
using System.Linq;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Threading;
using Avalonia.VisualTree;
using AvaloniaDialogs.Views;
using pos.Models.EventArgs;
using Pos.Models;

namespace Pos.Dialogs;

public partial class DiscountDialog : BaseDialog
{
    readonly DiscountDialogViewModel viewModel;

    public DiscountDialog(long? selectedLineItemId)
    {
        InitializeComponent();
        viewModel = new DiscountDialogViewModel(selectedLineItemId);
        DataContext = viewModel;
        viewModel.GetDiscounts().ContinueWith(_ => Dispatcher.UIThread.Post(() => ShowSelectedDiscount()));
    }

    private void CloseDialog(object sender, RoutedEventArgs args) => Close(EventArgs.Empty);

    public async void ManageDiscountButton_Click(object? sender, RoutedEventArgs args)
    {
        if (sender is Button button && button.Tag is Discount discount)
        {
            if ((string)button.Content! == "Apply")
            {
                if (!viewModel.CanApplyDiscount())
                {
                    MainWindow.NotificationMessage(sender, new NotificationEventArgs()
                    {
                        Message = "Cannot apply discount.",
                        NotifType = Util.Constants.NotifType.Error
                    });
                    return;
                }
                else if (viewModel._isManagingDiscount)
                {
                    MainWindow.NotificationMessage(this, new NotificationEventArgs
                    {
                        Message = "Discount is currently applying...",
                        NotifType = Util.Constants.NotifType.Error
                    });
                    return;
                }
                Task applyDiscountTask = viewModel.ApplyDiscount(discount);
                await applyDiscountTask;
                if (applyDiscountTask.IsCompletedSuccessfully)
                    button.Classes.Add("Danger");
            }
            else
            {
                if (viewModel._isManagingDiscount)
                {
                    MainWindow.NotificationMessage(this, new NotificationEventArgs
                    {
                        Message = "Discount is currently removing...",
                        NotifType = Util.Constants.NotifType.Error
                    });
                    return;
                }

                Task removeDiscountTask = viewModel.RemoveDiscount(discount);
                await removeDiscountTask;
                if (removeDiscountTask.IsCompletedSuccessfully)
                    button.Classes.Remove("Danger");
            }
        }
    }

    private void ShowSelectedDiscount()
    {
        var applyDiscountButtonList = DiscountItemsControl
            .GetVisualDescendants()
            .OfType<Button>()
            .Where(button => viewModel.AppliedDiscountId == (button.Tag as Discount)!.Id)
            .ToList();
        foreach (Button button in applyDiscountButtonList)
            button.Classes.Add("Danger");
    }
}