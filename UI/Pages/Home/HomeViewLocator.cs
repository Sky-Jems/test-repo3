using System;
using Pos.Pages.Home.Views.Categories;
using Pos.Pages.Home.Views.Menu;
using ReactiveUI;

namespace Pos.Pages.Home;

public class HomeViewLocator : IViewLocator
{
    public IViewFor ResolveView<T>(T? viewModel, string? contract) => viewModel switch
    {
        CategoriesViewModel context => new Categories { },
        MenuViewModel context => new Menu { },
        _ => throw new ArgumentOutOfRangeException(nameof(viewModel))
    };
}