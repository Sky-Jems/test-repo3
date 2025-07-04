using System;
using System.Collections.Generic;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Interactivity;
using Avalonia.ReactiveUI;
using Avalonia.VisualTree;
using pos.Extensions;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.Orders;

public partial class MainView : ReactiveUserControl<MainViewModel>
{
    public MainView()
    {
        InitializeComponent();
        this.WhenActivated(disposables =>
        {
            ViewModel!.StartDate = new DateTimeOffset(DateTime.UtcNow).DayStart();
            ViewModel.EndDate = new DateTimeOffset(DateTime.UtcNow).DayEnd();
            UpdateDateTimeButtonLabel();
            ViewModel.FilteredOrderTransactionsCommand.Execute();
        });
    }

    private void UpdateDateTimeButtonLabel()
    {
        startRangeText.Text = ViewModel!.StartDate!.Value.ToString("MMM d, yyyy");
        endRangeText.Text = ViewModel.EndDate!.Value.ToString("MMM d, yyyy");
    }

    private void DateRangeFlyOut_Opened(object sender, EventArgs e)
    {
        // make errors reappear
        if (ViewModel!.StartDate.HasValue)
        {
            DateTimeOffset startDate = ViewModel.StartDate.Value;
            startDatePicker.SelectedDate = null;
            startDatePicker.SelectedDate = startDate;
        }
        if (ViewModel.EndDate.HasValue)
        {
            DateTimeOffset endDate = ViewModel.EndDate.Value;
            endDatePicker.SelectedDate = null;
            endDatePicker.SelectedDate = endDate;
        }
    }

    private void ApplyDateFilterButton_Click(object sender, RoutedEventArgs args)
    {
        UpdateDateTimeButtonLabel();
        dateRangeFlyoutButton.Flyout!.Hide();
        ViewModel!.FilteredOrderTransactionsCommand.Execute();
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

        OrderTransaction CurrentRowData = (OrderTransaction)args.Row.DataContext!;
        ViewModel!.PopulateOrderCartPanel(CurrentRowData.Id);
    }

    private void AddOrdersButton_Click(object sender, RoutedEventArgs args)
    {
        Button button = (Button)sender;
        OrderTransaction CurrentRowData = (OrderTransaction)button.FindAncestorOfType<DataGridRow>()!.DataContext!;
        ViewModel!.PopulateOrderCartPanel(CurrentRowData.Id);
        MessageBus.Current.SendMessage(new SelectedTabIndexMessage(0));
    }
}