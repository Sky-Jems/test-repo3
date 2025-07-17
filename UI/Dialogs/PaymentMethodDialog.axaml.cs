using System;
using System.Linq;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Interactivity;
using AvaloniaDialogs.Views;

namespace Pos.Dialogs;

public partial class PaymentMethodDialog : BaseDialog<string>
{
    private PaymentMethodDialogViewModel vm { get; set; }
    public PaymentMethodDialog()
    {
        InitializeComponent();
        DataContext = new PaymentMethodDialogViewModel();
        vm = DataContext as PaymentMethodDialogViewModel;
        PriceTextBox.AddHandler(TextInputEvent, PriceTextBox_TextInput, RoutingStrategies.Tunnel);
    }

    private void CloseDialog(object sender, RoutedEventArgs args) => Close();

    private void ConfirmButton_Click(object? sender, RoutedEventArgs e)
    {
        if (sender is Button)
        {
            vm?.CompletePaymentCommand?.Execute().Subscribe();
        }
    }

    private void PaymentMethodClicked(object? sender, RoutedEventArgs args)
    {
        if (vm is not null && sender is Button btn)
        {
            vm._cartService.PaymentMethod = (string)btn.Tag;
        }
    }

    private void PriceTextBox_TextInput(object? sender, TextInputEventArgs e)
    {
        if (!e.Text.All(c => char.IsDigit(c) || c == '.'))
        {
            e.Handled = true;
        }
        else if (e.Text == "." && ((sender as TextBox)?.Text.Contains(".") ?? false))
        {
            e.Handled = true;
        }
    }
}