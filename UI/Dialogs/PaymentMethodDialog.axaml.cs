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

    private void PaymentMethodRadioButton_Checked(object sender, RoutedEventArgs args)
    {
        if (args.Source is RadioButton rb && rb.Tag is string method)
        {
            selectedPaymentMethod = method;
            referenceSection.IsVisible = method != "cash";
            confirmButton.IsEnabled = true;
        }
    }

    private async void ConfirmButton_Click(object? sender, RoutedEventArgs e)
    {
        if (selectedPaymentMethod is not null)
            Close(selectedPaymentMethod);
    }
}