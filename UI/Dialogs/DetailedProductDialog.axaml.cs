using Avalonia.Interactivity;
using AvaloniaDialogs.Views;
using Pos.Dialogs;

namespace pos.Dialogs;

public partial class DetailedProductDialog : BaseDialog
{
    public DetailedProductDialog()
    {
        InitializeComponent();
        DataContext = new DetailedProductDialogViewModel();
    }

    private void CloseDialog(object sender, RoutedEventArgs args) => Close();
}