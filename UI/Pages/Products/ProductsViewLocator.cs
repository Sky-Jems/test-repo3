using System;
using ReactiveUI;

namespace Pos.Pages.Products;

public class ProductsViewLocator : IViewLocator
{
    public IViewFor ResolveView<T>(T? viewModel, string? contract) => viewModel switch
    {
        MainViewModel context => new MainView { DataContext = context },
        CreateProductViewModel context => new CreateProductView { },
        _ => throw new ArgumentOutOfRangeException(nameof(viewModel))
    };
}