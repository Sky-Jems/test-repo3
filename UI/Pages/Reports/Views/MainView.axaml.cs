using System;
using System.Collections.Generic;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Interactivity;
using Avalonia.ReactiveUI;
using Pos.Models;

namespace Pos.Pages.Reports;

public partial class MainView : ReactiveUserControl<MainViewModel>
{
    public MainView()
    {
        InitializeComponent();
    }

    private void DateRangeFlyOut_Opened(object sender, EventArgs e)
    {
        // make errors reappear
        if (ViewModel!.StartDate != null)
        {
            DateTimeOffset startDate = (DateTimeOffset)ViewModel.StartDate;
            startDatePicker.SelectedDate = null;
            startDatePicker.SelectedDate = startDate;
        }
        if (ViewModel.EndDate != null)
        {
            DateTimeOffset endDate = (DateTimeOffset)ViewModel.EndDate;
            endDatePicker.SelectedDate = null;
            endDatePicker.SelectedDate = endDate;
        }
    }

    private void ApplyDateFilterButton_Click(object sender, RoutedEventArgs args)
    {
        startRangeText.Text = ViewModel!.StartDate!.Value.ToString("MMM d, yyyy");
        endRangeText.Text = ViewModel.EndDate!.Value.ToString("MMM d, yyyy");
        dateRangeFlyoutButton.Flyout!.Hide();
        ViewModel.FilteredOrderCommand.Execute();
    }

    private void DataGrid_PointerReleased(object sender, PointerReleasedEventArgs args)
    {
        // prevent triggering PointerReleased of SplitView
        // which causes it to hide immediately after DataGrid_CellPointerPressed
        args.Handled = true;
    }

    private void DataGrid_CellPointerPressed(object sender, DataGridCellPointerPressedEventArgs args)
    {
        // prevent showing order cart panel when actions icon button is clicked
        if (args.Column.Tag?.ToString() == "Actions")
        {
            args.PointerPressedEventArgs.Handled = true;
            return;
        }

        orderDetailsPane.IsPaneOpen = true;

        OrderReport CurrentRowData = (OrderReport)args.Row.DataContext!;
        List<LineItemDto> lineItems = CurrentRowData.LineItems;
        ViewModel!.OrderCartPanelViewModel.OrderList.Clear();
        foreach (LineItemDto lineItem in lineItems)
        {
            // update SKU with the data coming from the order service
            ViewModel.OrderCartPanelViewModel.OrderList.Add(new LineItem
            {
                ProductId = lineItem.ProductId,
                // Sku = "test",
                Quantity = lineItem.Quantity,
                Price = lineItem.Price
            });
        }
        // ViewModel.OrderCartPanelViewModel.CartTotal = CurrentRowData.Total;
    }
}