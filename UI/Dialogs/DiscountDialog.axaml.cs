using AvaloniaDialogs.Views;

namespace Pos.Dialogs;

public partial class DiscountDialog : BaseDialog
{
    public DiscountDialog()
    {
        InitializeComponent();
        DataContext = new DiscountDialogViewModel();
    }
}