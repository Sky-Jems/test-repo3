using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Reactive;
using System.Threading.Tasks;
using Avalonia.Data;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using Pos.Controls;
using Pos.Models;
using ReactiveUI;
using pos.Extensions;

namespace Pos.Pages.Reports;

public partial class MainViewModel : ReactiveObject, IRoutableViewModel
{
    public string? UrlPathSegment => throw new NotImplementedException();
    public IScreen HostScreen { get; }

    private readonly IOrderReportService _orderReportService;

    private DateTimeOffset? _StartDate;
    public DateTimeOffset? StartDate
    {
        get => _StartDate;
        set
        {
            _StartDate = value;
            this.RaisePropertyChanged(nameof(StartDate));

            IsDateFilterable = false;
            if (value > EndDate)
                throw new DataValidationException("Start date cannot be late than end date");
            else if (EndDate != null)
                IsDateFilterable = true;

            this.RaisePropertyChanged(nameof(EndDate));
        }
    }

    private DateTimeOffset? _EndDate;
    public DateTimeOffset? EndDate
    {
        get => _EndDate;
        set
        {
            _EndDate = value;
            this.RaisePropertyChanged(nameof(EndDate));

            IsDateFilterable = false;
            if (value < StartDate)
                throw new DataValidationException("End date cannot be early than start date");

            IsDateFilterable = true;
            this.RaisePropertyChanged(nameof(StartDate));
        }
    }

    private bool _IsDateFilterable;
    public bool IsDateFilterable
    {
        get => _IsDateFilterable;
        set => this.RaiseAndSetIfChanged(ref _IsDateFilterable, value);
    }

    private OrderReports _OrderReports = new();
    public OrderReports OrderReports
    {
        get => _OrderReports;
        set => this.RaiseAndSetIfChanged(ref _OrderReports, value);
    }

    public OrderCartPanelViewModel OrderCartPanelViewModel { get; set; } = new OrderCartPanelViewModel();
    public ReactiveCommand<Unit, Unit> SummaryButtonCommand { get; }
    public ReactiveCommand<Unit, Unit> FilteredOrderCommand { get; }
    public ReactiveCommand<Unit, Unit> LoadOrdersCommand { get; }

    public MainViewModel(IScreen screen, OrderReportService orderReportService)
    {
        HostScreen = screen;
        _orderReportService = orderReportService;
        FilteredOrderCommand = ReactiveCommand.CreateFromTask(LoadFilteredOrdersAsync);
        SummaryButtonCommand = ReactiveCommand.Create(() => MessageBus.Current.SendMessage(new SelectedTabIndexMessage(0)));
        // LoadOrdersAsync();
        LoadOrdersCommand = ReactiveCommand.CreateFromTask(LoadOrdersAsync);
        LoadOrdersCommand.Execute().Subscribe();
    }

    private async Task LoadFilteredOrdersAsync()
    {
        try
        {
            OrderReports = null;
            if (StartDate != null)
            {
                string start = StartDate.DayStart().ToISO8601().ToUrlEncoded();
                string end = EndDate.DayEnd().ToISO8601().ToUrlEncoded();
                OrderReports = await _orderReportService.GetFilteredOrderReports(start, end);
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
        }
    }

    private async Task LoadOrdersAsync()
    {
        try
        {
            OrderReports = await _orderReportService.GetOrderReports();
        }
        catch (Exception e)
        {
            Debug.WriteLine(e);
            throw;
        }
    }
}