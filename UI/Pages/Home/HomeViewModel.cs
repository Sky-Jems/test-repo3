using System;
using Pos.Pages.Home.Views.Categories;
using ReactiveUI;

namespace Pos.Pages.Home;

public partial class HomeViewModel : ReactiveObject, IScreen
{
    public string? UrlPathSegment => throw new NotImplementedException();
    public RoutingState Router { get; } = new RoutingState();

    public HomeViewModel() {
        Router.Navigate.Execute(new CategoriesViewModel(this));
    }
}