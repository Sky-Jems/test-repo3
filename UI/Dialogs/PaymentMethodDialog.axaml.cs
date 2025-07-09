using System;
using Avalonia.Controls;
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
    }

    private void CloseDialogButton_Click(object sender, RoutedEventArgs args) => Close();

    private async void ConfirmButton_Click(object? sender, RoutedEventArgs e)
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
}