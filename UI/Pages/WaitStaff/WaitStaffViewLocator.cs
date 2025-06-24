using System;
using ReactiveUI;

namespace Pos.Pages.WaitStaff;

public class WaitStaffViewLocator : IViewLocator
{
    public IViewFor ResolveView<T>(T? viewModel, string? contract) => viewModel switch
    {
        CategoriesViewModel context => new CategoriesView { },
        MenuViewModel context => new MenuView { },
        _ => throw new ArgumentOutOfRangeException(nameof(viewModel))
    };
}