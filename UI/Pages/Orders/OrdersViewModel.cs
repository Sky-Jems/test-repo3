using System;
using System.Reactive;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using pos.Handlers.Interfaces;
using ReactiveUI;

namespace Pos.Pages.Orders;

public partial class OrdersViewModel : ReactiveObject, IScreen
{
    public string? UrlPathSegment => throw new NotImplementedException();
    public RoutingState Router { get; } = new RoutingState();

    public ReactiveCommand<string, Unit> GoToMain { get; }

    public OrdersViewModel()
    {
        var httpClient = ServiceLocator.Services.GetRequiredService<IHttpHandler>();
        GoToMain = ReactiveCommand.Create<string>(status => Router.Navigate.Execute(new MainViewModel(this, new OrderTransactionService(httpClient), status)));
    }
}