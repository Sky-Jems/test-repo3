using System;
using Pos.Pages.Home.Views.Categories;
using Pos.Pages.Home.Views.Menu;
using Pos.Pages.Home.Views.Options;
using ReactiveUI;

namespace Pos.Pages.Home;

public class HomeViewLocator : IViewLocator
{
    public IViewFor ResolveView<T>(T? viewModel, string? contract) => viewModel switch
    {
        CategoriesViewModel context => new Categories { },
        MenuViewModel context => new Menu { },
        OptionsViewModel context => new Options { },
        _ => throw new ArgumentOutOfRangeException(nameof(viewModel))
    };
}