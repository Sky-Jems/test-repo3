using System;
using System.Linq;
using System.Reactive;
using System.Threading.Tasks;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Interactivity;
using Pos.Dialogs;
using Pos.Models;
using ReactiveUI;
using static Pos.Util.Constants;

namespace Pos.Controls;

public partial class OrderCartPanel : UserControl
{
    public static readonly StyledProperty<bool> IsEditableProperty =
        AvaloniaProperty.Register<CategoryCard, bool>(nameof(IsEditable));

    public bool IsEditable
    {
        get => GetValue(IsEditableProperty);
        set => SetValue(IsEditableProperty, value);
    }

    public static readonly StyledProperty<bool> ShowButtonsProperty =
        AvaloniaProperty.Register<CategoryCard, bool>(nameof(ShowButtons), true);

    public bool ShowButtons
    {
        get => GetValue(ShowButtonsProperty);
        set => SetValue(ShowButtonsProperty, value);
    }

    public static readonly StyledProperty<string> SummaryButtonTextProperty =
        AvaloniaProperty.Register<CategoryCard, string>(nameof(SummaryButtonText), "Pay Order");

    public string SummaryButtonText
    {
        get => GetValue(SummaryButtonTextProperty);
        set => SetValue(SummaryButtonTextProperty, value);
    }

    public static readonly StyledProperty<ReactiveCommand<Unit, Unit>> SummaryButtonCommandProperty =
        AvaloniaProperty.Register<OrderCartPanel, ReactiveCommand<Unit, Unit>>(nameof(SummaryButtonCommand));

    public ReactiveCommand<Unit, Unit> SummaryButtonCommand
    {
        get => GetValue(SummaryButtonCommandProperty);
        set => SetValue(SummaryButtonCommandProperty, value);
    }

    private OrderCartPanelViewModel viewModel;

    public OrderCartPanel()
    {
        InitializeComponent();
    }

    protected override void OnAttachedToVisualTree(VisualTreeAttachmentEventArgs e)
    {
        base.OnAttachedToVisualTree(e);
        viewModel = (OrderCartPanelViewModel)DataContext!;
    }

    private async void ApplyDiscountButton_Click(object sender, RoutedEventArgs args)
    {
        if (await ShowLockedDialogIfNotModifiable(viewModel)) return;
        if (viewModel.OrderList.Count == 0 || viewModel?.DiscountOrder?.lineItems != null)
        {
            InfoDialog dialog = new()
            {
                Title = viewModel.OrderList.Count == 0 ? "Failed to apply discount" : "Discount already applied",
                Message = viewModel.OrderList.Count == 0 ? "Please select a product first before applying a discount." : "You can't apply order discount if an item discount is already applied.",
                ButtonText = "OK"
            };
            await dialog.ShowAsync();
            return;
        }
        else
        {
            await DisplayDiscountDialog(sender, OrderDiscountType.Order);
        }
    }

    private async Task<bool> ShowLockedDialogIfNotModifiable(OrderCartPanelViewModel? vm)
    {
        if (vm == null || vm.CanModifyItems)
            return false;

        var lockedDialog = new InfoDialog
        {
            Title = "Can't modify order",
            Message = "Items cannot be modified because the order is already completed or partially paid.",
            ButtonText = "OK"
        };

        await lockedDialog.ShowAsync();
        return true;
    }

    private async void OnPlusClick(object? sender, RoutedEventArgs e)
    {
        if (sender is Button button && button.Tag is LineItem lineItem)
        {
            if (await ShowLockedDialogIfNotModifiable(viewModel)) return;

            viewModel.ClickPlusCommand.Execute(lineItem).Subscribe();
        }
    }

    private async void OnMinusClick(object? sender, RoutedEventArgs e)
    {
        if (sender is Button button && button.Tag is LineItem lineItem)
        {
            if (await ShowLockedDialogIfNotModifiable(viewModel)) return;

            viewModel.ClickMinusCommand.Execute(lineItem).Subscribe();
        }
    }

    private async void OnCustomerNameLostFocus(object? sender, RoutedEventArgs e)
    {
        if (await ShowLockedDialogIfNotModifiable(viewModel)) return;

        viewModel.UpdateOrderCommand.Execute().Subscribe();
    }

    private async void OnClearButtonClick(object? sender, RoutedEventArgs e)
    {
        if (sender is not Button) return;

        if (await ShowLockedDialogIfNotModifiable(viewModel)) return;

        if (!viewModel.OrderList.Any())
        {
            var emptyDialog = new InfoDialog
            {
                Title = "All clear",
                Message = "No items to clear.",
                ButtonText = "OK"
            };

            await emptyDialog.ShowAsync();
            return;
        }

        var dialog = new ConfirmationDialog
        {
            Title = "Clear all items?",
            Message = "Are you sure you want to clear all items?",
            PositiveText = "Clear",
            NegativeText = "Cancel"
        };
        dialog.FindControl<Button>("PositiveButton")!.Classes.Add("Danger");

        if ((await dialog.ShowAsync()).GetValueOrDefault())
            viewModel.ClearLineItemsCommand.Execute().Subscribe();
    }

    private async void OnPayLaterButtonClick(object? sender, RoutedEventArgs e)
    {
        if (sender is not Button) return;

        if (viewModel == null) return;

        if (!viewModel.OrderList.Any())
        {
            var emptyDialog = new InfoDialog
            {
                Title = "Can't pay order",
                Message = "Please add at least one item.",
                ButtonText = "OK"
            };

            await emptyDialog.ShowAsync();
            return;
        }

        viewModel.PayLaterCommand?.Execute().Subscribe();
    }

    private void CartItem_PointerPressed(object sender, PointerPressedEventArgs e)
    {
        if (sender is Border border && border.Tag is LineItem lineItem)
            viewModel.NavigateToMenuCommand.Execute(lineItem).Subscribe();
    }

    private async void DiscountLineItemButton_Click(object? sender, RoutedEventArgs args)
    {
        if (await ShowLockedDialogIfNotModifiable(viewModel)) return;
        if (viewModel?.DiscountOrder?.Discount?.Id != null)
        {
            InfoDialog dialog = new()
            {
                Title = "Discount already applied",
                Message = "You can't apply an item discount if order discount is already applied.",
                ButtonText = "OK"
            };
            await dialog.ShowAsync();
            return;
        }
        int lineItemId = Convert.ToInt32(((Button)sender!).Tag);
        await DisplayDiscountDialog(sender, OrderDiscountType.LineItem, lineItemId);
    }

    private async Task DisplayDiscountDialog(object? sender, OrderDiscountType discountType, long? lineItemId = null)
    {
        // fetch order dialog order id 
        DiscountDialog dialog = new(lineItemId);
        await dialog.ShowAsync();
    }

    private async void Border_Holding(object? sender, HoldingRoutedEventArgs args)
    {
        if (args.HoldingState == HoldingState.Completed)
        {
            ConfirmationDialog dialog = new()
            {
                Title = "Remove item from cart?",
                Message = "Are you sure you want to remove this item from your cart?",
                PositiveText = "Remove",
                NegativeText = "Cancel"
            };
            dialog.FindControl<Button>("PositiveButton")!.Classes.Add("Danger");

            var result = await dialog.ShowAsync();

            if (result.HasValue && result.Value)
            {
                var border = sender as Border;
                var lineItem = border?.Tag as LineItem;

                var vm = DataContext as OrderCartPanelViewModel;
                vm?.RemoveLineItemCommand?.Execute(lineItem)?.Subscribe();
            }
        }
    }

    private async void OnPayOrderButtonClick(object? sender, RoutedEventArgs e)
    {
        if (sender is not Button) return;

        if (viewModel == null) return;

        if (!viewModel.OrderList.Any())
        {
            var emptyDialog = new InfoDialog
            {
                Title = "Can't pay order",
                Message = "Please add at least one item.",
                ButtonText = "OK"
            };

            await emptyDialog.ShowAsync();
            return;
        }
        viewModel.PayOrderCommand.Execute().Subscribe();
    }
}
