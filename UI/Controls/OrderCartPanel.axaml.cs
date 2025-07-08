using System;
using System.Linq;
using System.Reactive;
using System.Threading.Tasks;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Interactivity;
using AvaloniaDialogs.Views;
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

    public OrderCartPanel()
    {
        InitializeComponent();
    }

    private async void ApplyDiscountButton_Click(object sender, RoutedEventArgs args)
    {
        await DisplayDiscountDialog(sender, OrderDiscountType.Order);
    }

    private async Task<bool> ShowLockedDialogIfNotModifiable(OrderCartPanelViewModel? vm)
    {
        if (vm == null || vm.CanModifyItems)
            return false;

        var lockedDialog = new SingleActionDialog
        {
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
            var vm = DataContext as OrderCartPanelViewModel;
            if (await ShowLockedDialogIfNotModifiable(vm)) return;

            vm?.ClickPlusCommand?.Execute(lineItem)?.Subscribe();
        }
    }
    
    private async void OnMinusClick(object? sender, RoutedEventArgs e)
    {
        if (sender is Button button && button.Tag is LineItem lineItem)
        {
            var vm = DataContext as OrderCartPanelViewModel;
            if (await ShowLockedDialogIfNotModifiable(vm)) return;

            vm?.ClickMinusCommand?.Execute(lineItem)?.Subscribe();
        }
    }

    private async void OnRemoveLineItemButtonClick(object? sender, RoutedEventArgs e)
    {
        if (sender is Button button && button.Tag is LineItem lineItem)
        {
            var vm = DataContext as OrderCartPanelViewModel;
            if (await ShowLockedDialogIfNotModifiable(vm)) return;

            vm?.RemoveLineItemCommand?.Execute(lineItem)?.Subscribe();
        }
    }

    private async void OnCustomerNameLostFocus(object? sender, RoutedEventArgs e)
    {
        var vm = DataContext as OrderCartPanelViewModel;
        if (await ShowLockedDialogIfNotModifiable(vm)) return;

        if (!string.IsNullOrWhiteSpace(vm!.Customer))
        {
            vm.UpdateOrderCommand?.Execute().Subscribe();    
        }
    }

    private async void OnClearButtonClick(object? sender, RoutedEventArgs e)
    {
        if (sender is not Button) return;

        var vm = DataContext as OrderCartPanelViewModel;
        if (await ShowLockedDialogIfNotModifiable(vm)) return;

        if (!vm!.OrderList.Any())
        {
            var emptyDialog = new SingleActionDialog
            {
                Message = "No items to clear.",
                ButtonText = "OK"
            };

            await emptyDialog.ShowAsync();
            return;
        }

        var dialog = new TwofoldDialog
        {
            Message = "Are you sure you want to clear all items?",
            PositiveText = "Yes",
            NegativeText = "No"
        };

        if ((await dialog.ShowAsync()).GetValueOrDefault())
        {
            vm.ClearLineItemsCommand?.Execute()?.Subscribe();
        }
    }

    private async void OnPayLaterButtonClick(object? sender, RoutedEventArgs e)
    {
        if (sender is not Button) return;

        var vm = DataContext as OrderCartPanelViewModel;
        if (vm == null) return;

        if (!vm.OrderList.Any())
        {
            var emptyDialog = new SingleActionDialog
            {
                Message = "Please add at least one item.",
                ButtonText = "OK"
            };

            await emptyDialog.ShowAsync();
            return;
        }

        vm.PayLaterCommand?.Execute().Subscribe();
    }

    private void CartItem_PointerPressed(object sender, PointerPressedEventArgs e)
    {
        if (sender is Border border && border.Tag is LineItem lineItem)
        {
            var vm = DataContext as OrderCartPanelViewModel;
            vm?.NavigateToMenuCommand?.Execute(lineItem)?.Subscribe();
        }
    }

    private async void DiscountLineItemButton_Click(object? sender, RoutedEventArgs args)
    {
        await DisplayDiscountDialog(sender, OrderDiscountType.LineItem);
    }

    private async Task DisplayDiscountDialog(object? sender, OrderDiscountType discountType)
    {
        // TODO: handle fetching discounts per line item and order.
        Discount[] myDiscountList = [
            new Discount { Name = "₱100 off", Description = "Applies to one product" },
            new Discount { Name = "%20 off", Description = "Minimum spend of ₱50" },

        ];

        DiscountDialog dialog = new();
        foreach (var discount in myDiscountList)
            (dialog.DataContext as DiscountDialogViewModel)!.DiscountList.Add(discount);
        await dialog.ShowAsync();


        // TODO: After selecting discount, apply to label
        Button button = (Button)sender;
        button.Classes.Add("Success");
        button.Content = "Applied %";
    }

    private async void Border_Holding(object? sender, HoldingRoutedEventArgs args)
    {
        if (args.HoldingState == HoldingState.Completed)
        {
            TwofoldDialog dialog = new()
            {
                Message = "Are you sure you want to remove this item from your cart?",
                PositiveText = "Yes, Remove",
                NegativeText = "Cancel"
            };
            dialog.FindControl<Button>("PositiveButton")!.Classes.Add("Danger");
            await dialog.ShowAsync();
        }
    }

    private async void OnPayOrderButtonClick(object? sender, RoutedEventArgs e)
    {
        if (sender is not Button) return;

        var vm = DataContext as OrderCartPanelViewModel;
        if (vm == null) return;

        if (!vm.OrderList.Any())
        {
            var emptyDialog = new SingleActionDialog
            {
                Message = "Please add at least one item.",
                ButtonText = "OK"
            };

            await emptyDialog.ShowAsync();
            return;
        }
        
        vm.PayOrderCommand?.Execute().Subscribe();
    }
}
