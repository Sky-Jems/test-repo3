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
    }

    private void PaymentMethodRadioButton_Checked(object sender, RoutedEventArgs args)
    {
        if (sender is RadioButton rb && rb.Tag is string method)
        {
            selectedPaymentMethod = method;
            confirmButton.IsEnabled = true;
        }
    }

    private async void ConfirmButton_Click(object? sender, RoutedEventArgs e)
    {
        if (selectedPaymentMethod is not null)
            Close(selectedPaymentMethod);
    }
}