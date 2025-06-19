using System;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using pos.Handlers.Interfaces;
using ReactiveUI;

namespace Pos.Pages.Reports;

public partial class ReportsViewModel : ReactiveObject, IScreen
{
    public string? UrlPathSegment => throw new NotImplementedException();
    public RoutingState Router { get; } = new RoutingState();

    public ReportsViewModel()
    {
        var httpClient = ServiceLocator.Services.GetRequiredService<IHttpHandler>();
        Router.Navigate.Execute(new MainViewModel(this, new OrderReportService(httpClient)));
    }
}