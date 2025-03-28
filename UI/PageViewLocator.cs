using System;
using Pos.Pages.Home;
using ReactiveUI;

namespace Pos;

public class PageViewLocator : IViewLocator
{
    public IViewFor ResolveView<T>(T? viewModel, string? contract) => viewModel switch
    {
        HomeViewModel context => new Home { },
        _ => throw new ArgumentOutOfRangeException(nameof(viewModel))
    };
}