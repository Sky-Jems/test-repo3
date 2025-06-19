using System;
using ReactiveUI;

namespace Pos.Pages.Products;

public partial class ProductsViewModel : ReactiveObject, IScreen
{
    public string? UrlPathSegment => throw new NotImplementedException();
    public RoutingState Router { get; } = new RoutingState();

    public ProductsViewModel()
    {
        Router.Navigate.Execute(new MainViewModel(this));
    }
}