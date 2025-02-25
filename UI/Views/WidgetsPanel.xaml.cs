using src.ViewModels;

namespace POS.Views;

public partial class WidgetsPanel
{
    public event EventHandler? ShowProducts;
    public WidgetsPanel()
    {
        InitializeComponent();
        BindingContext = new CategoriesViewModel();
    }
    private void CategoryButtonClicked(object sender, EventArgs e)
    {
        ShowProducts?.Invoke(this, EventArgs.Empty);
    }
}
