using System;
using System.Reactive;
using Pos.Pages.Home.Views.Categories;
using Pos.Pages.Home.Views.Menu;
using ReactiveUI;

namespace Pos.Pages.Home;

public partial class HomeViewModel : ReactiveObject, IRoutableViewModel, IScreen
{
    public string? UrlPathSegment => throw new System.NotImplementedException();
    public IScreen HostScreen { get; }
    public RoutingState Router { get; } = new RoutingState();

    public HomeViewModel(IScreen screen) {
        this.HostScreen = screen;
        Router.Navigate.Execute(new CategoriesViewModel(this));
    }
}