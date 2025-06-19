using System;
using System.Windows.Input;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Controls.Notifications;
using Avalonia.Interactivity;
using Avalonia.ReactiveUI;
using Avalonia.Threading;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using pos.Models.EventArgs;
using Pos.Models;

namespace Pos.Pages.WaitStaff;

public partial class WaitStaffView : ReactiveUserControl<WaitStaffViewModel>
{
    public static readonly StyledProperty<LineItem> LineItemProperty =
        AvaloniaProperty.Register<WaitStaffView, LineItem>(nameof(LineItem));

    public LineItem LineItem
    {
        get => GetValue(LineItemProperty);
        set => SetValue(LineItemProperty, value);
    }

    public static readonly StyledProperty<ICommand> CartCommandProperty =
        AvaloniaProperty.Register<WaitStaffView, ICommand>(nameof(CartCommand));

    public ICommand CartCommand
    {
        get => GetValue(CartCommandProperty);
        set => SetValue(CartCommandProperty, value);
    }

    private WindowNotificationManager? _manager;

    // Default constructor (required for XAML)
    // Todo: This can also be done through Dependency Injection > preferred for MVVM
    public WaitStaffView() : this(ServiceLocator.Services.GetRequiredService<ICategoryService>(), ServiceLocator.Services.GetRequiredService<IProductService>())
    {
    }

    public WaitStaffView(ICategoryService categoryService, IProductService productService)
    {
        InitializeComponent();
        DataContext = new WaitStaffViewModel(categoryService, productService);
    }

    protected override void OnAttachedToVisualTree(VisualTreeAttachmentEventArgs e)
    {
        base.OnAttachedToVisualTree(e);
        var topLevel = TopLevel.GetTopLevel(this);
        _manager = new WindowNotificationManager(topLevel)
        {
            MaxItems = 3,
            Position = NotificationPosition.TopRight
        };
        Dispatcher.UIThread.Post(() =>
        {
            (DataContext as WaitStaffViewModel)!.TriggerNotif += NotificationMessage;
        });
    }

    private void NotificationMessage(object? sender, NotificationEventArgs args)
    {
        _manager?.Show(
            new Notification("New Message", args.Message),
            (NotificationType)args.NotifType,
            TimeSpan.FromSeconds(1),
            classes: args.Classes
        );
    }


    // public async void PlaceOrderButton_Click(object sender, RoutedEventArgs args)
    // {
    //     PaymentMethodDialog dialog = new();
    //     string selectedPaymentMethod = (await dialog.ShowAsync()).GetValueOrDefault();
    //     if (!string.IsNullOrEmpty(selectedPaymentMethod))
    //     {
    //         var viewModel = DataContext as HomeViewModel;
    //         await viewModel!.PlaceOrderAsync(selectedPaymentMethod);
    //         _manager!.Show(new Notification(
    //             "",
    //             "Order has been placed."),
    //             type: NotificationType.Success,
    //             classes: ["Light"]
    //         );
    //     }
    // }
    // public void LogoutMenuItem_Click(object sender, RoutedEventArgs args)
    // {

    // }
}
