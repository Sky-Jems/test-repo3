using Avalonia;
using Avalonia.ReactiveUI;
using ReactiveUI;
using System;

namespace Pos.Pages.Orders;

public partial class OrdersView : ReactiveUserControl<OrdersViewModel>
{
    public static readonly StyledProperty<string> StatusProperty =
        AvaloniaProperty.Register<OrdersView, string>(nameof(Status));

    private OrdersViewModel viewModel;

    public string Status
    {
        get => GetValue(StatusProperty);
        set => SetValue(StatusProperty, value);
    }

    public OrdersView()
    {
        InitializeComponent();
        viewModel = new OrdersViewModel();
        DataContext = viewModel;

        this.WhenAnyValue(x => x.Status).Subscribe(value => viewModel.GoToMain.Execute(value).Subscribe());
    }
}