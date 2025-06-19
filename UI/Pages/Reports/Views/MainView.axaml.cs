using System;
using System.Collections.Generic;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Interactivity;
using Avalonia.ReactiveUI;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.Reports;

public partial class MainView : ReactiveUserControl<MainViewModel>
{
    public MainViewModel viewModel;

    public MainView()
    {
        InitializeComponent();
        this.WhenActivated(disposables => viewModel = (MainViewModel)DataContext!);
    }

    private void DateRangeFlyOut_Opened(object sender, EventArgs e)
    {
        // make errors reappear
        if (viewModel.StartDate != null)
        {
            DateTimeOffset startDate = (DateTimeOffset)viewModel.StartDate;
            startDatePicker.SelectedDate = null;
            startDatePicker.SelectedDate = startDate;
        }
        if (viewModel.EndDate != null)
        {
            DateTimeOffset endDate = (DateTimeOffset)viewModel.EndDate;
            endDatePicker.SelectedDate = null;
            endDatePicker.SelectedDate = endDate;
        }
    }

    private void ApplyDateFilterButton_Click(object sender, RoutedEventArgs args)
    {
        startRangeText.Text = viewModel.StartDate!.Value.ToString("MMM d, yyyy");
        endRangeText.Text = viewModel.EndDate!.Value.ToString("MMM d, yyyy");
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
        viewModel.OrderCartPanelViewModel.OrderList.Clear();
        foreach (LineItemDto lineItem in lineItems)
        {
            // update SKU with the data coming from the order service
            viewModel.OrderCartPanelViewModel.OrderList.Add(new LineItem
            {
                ProductId = lineItem.ProductId,
                Sku = "test",
                Quantity = lineItem.Quantity,
                Price = lineItem.Price
            });
        }
        viewModel.OrderCartPanelViewModel.CartTotal = CurrentRowData.Total;
    }
}