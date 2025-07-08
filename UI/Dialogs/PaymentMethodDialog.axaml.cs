using System;
using Avalonia.Controls;
using Avalonia.Interactivity;
using AvaloniaDialogs.Views;

namespace Pos.Dialogs;

public partial class PaymentMethodDialog : BaseDialog<string>
{
    private string? selectedPaymentMethod;

    public PaymentMethodDialog()
    {
        InitializeComponent();
        DataContext = new PaymentMethodDialogViewModel();
    }

    private void CloseDialogButton_Click(object sender, RoutedEventArgs args) => Close();

    private async void ConfirmButton_Click(object? sender, RoutedEventArgs e)
    {
        if (sender is Button)
        {
            var vm = DataContext as PaymentMethodDialogViewModel;
            vm?.CompletePaymentCommand?.Execute().Subscribe();
        }
    }
}