using System;
using ReactiveUI;

namespace Pos.Pages.Reports;

public class ReportsViewLocator : IViewLocator
{
    public IViewFor ResolveView<T>(T? viewModel, string? contract) => viewModel switch
    {
        MainViewModel context => new MainView { DataContext = context },
        _ => throw new ArgumentOutOfRangeException(nameof(viewModel))
    };
}