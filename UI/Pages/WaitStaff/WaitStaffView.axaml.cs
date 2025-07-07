using System;
using System.Windows.Input;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Controls.Notifications;
using Avalonia.ReactiveUI;
using Avalonia.Threading;
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

    public WaitStaffView()
    {
        InitializeComponent();
        DataContext = new WaitStaffViewModel();
    }

    protected override void OnAttachedToVisualTree(VisualTreeAttachmentEventArgs e)
    {
        base.OnAttachedToVisualTree(e);
        var topLevel = TopLevel.GetTopLevel(this);
        Dispatcher.UIThread.Post(() =>
        {
            (DataContext as WaitStaffViewModel)!.TriggerNotif += MainWindow.NotificationMessage;
        });
    }
}
