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
}