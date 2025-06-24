using System;
using System.Reactive;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Interactivity;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using Pos.Dialogs;
using pos.Extensions;
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

    public static readonly StyledProperty<string> SummaryButtonTextProperty =
        AvaloniaProperty.Register<CategoryCard, string>(nameof(SummaryButtonText), "PAY ORDER");

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

    private void OnRemoveLineItemButtonClick(object? sender, RoutedEventArgs e)
    {
        var cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        // TODO: use batch remove item instead
        cartService.RemoveItem((e.Source as Button).Tag as LineItem);
    }

    private void OnClearButtonClick(object? sender, RoutedEventArgs e)
    {
        var cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        cartService.Items.Clear();
    }

    private void OnPayLaterButtonClick(object? sender, RoutedEventArgs e)
    {
        var cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        cartService.ClearItems();
        cartService.OrderId = null;
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
