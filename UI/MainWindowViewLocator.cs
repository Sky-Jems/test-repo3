using System;
using Pos.Pages;
using ReactiveUI;

namespace Pos;

public class MainWindowViewLocator : IViewLocator
{
    public IViewFor ResolveView<T>(T? viewModel, string? contract) => viewModel switch
    {
        HomePageViewModel context => new HomePage { },
        _ => throw new ArgumentOutOfRangeException(nameof(viewModel))
    };
}
