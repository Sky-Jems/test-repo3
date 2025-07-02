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
        Discount[] myDiscountList = [
            new Discount { Name = "₱100 off", Description = "Applies to one product" },
            new Discount { Name = "%20 off", Description = "Minimum spend of ₱50" },

        ];

        DiscountDialog dialog = new();
        foreach (var discount in myDiscountList)
            (dialog.DataContext as DiscountDialogViewModel)!.DiscountList.Add(discount);
        await dialog.ShowAsync();
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


    private async void OnRemoveLineItemButtonClick(object? sender, RoutedEventArgs e)
    {
        if (sender is Button button && button.Tag is LineItem lineItem)
        {
            var vm = DataContext as OrderCartPanelViewModel;
            if (await ShowLockedDialogIfNotModifiable(vm)) return;

            vm?.RemoveLineItemCommand?.Execute(lineItem)?.Subscribe();
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
}
