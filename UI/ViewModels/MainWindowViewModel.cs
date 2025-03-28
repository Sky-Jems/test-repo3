using System;
using System.Reactive;
using Pos.Pages.Home;
using ReactiveUI;

namespace Pos.ViewModels;

public partial class MainWindowViewModel : ReactiveObject, IScreen
{
    public RoutingState Router { get; } = new RoutingState();
    public ReactiveCommand<Unit, IRoutableViewModel> GoBack => Router.NavigateBack;

    public MainWindowViewModel()
    {
        Router.Navigate.Execute(new HomeViewModel(this));
    }
}
