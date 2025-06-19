using Avalonia.ReactiveUI;

namespace Pos.Pages.Products;

public partial class ProductsView : ReactiveUserControl<ProductsViewModel>
{
    public ProductsView()
    {
        InitializeComponent();
        DataContext = new ProductsViewModel();
    }
}