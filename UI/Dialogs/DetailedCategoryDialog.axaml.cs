using Avalonia.Interactivity;
using AvaloniaDialogs.Views;
using Pos.Dialogs;

namespace pos.Dialogs;

public partial class DetailedCategoryDialog : BaseDialog
{
    public DetailedCategoryDialog()
    {
        InitializeComponent();
        DataContext = new DetailedCategoryDialogViewModel();
    }

    private void CloseDialog(object sender, RoutedEventArgs args) => Close();
}