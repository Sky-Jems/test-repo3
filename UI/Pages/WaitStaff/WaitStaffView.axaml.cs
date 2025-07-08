using System.Windows.Input;
using Avalonia;
using Avalonia.Controls;
using Avalonia.ReactiveUI;
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
        TopLevel.GetTopLevel(this);
        if (DataContext is WaitStaffViewModel vm)
        {
            vm.TriggerNotif -= MainWindow.NotificationMessage; // Prevent duplicates
            vm.TriggerNotif += MainWindow.NotificationMessage;
        }
    }
    
    protected override void OnDetachedFromVisualTree(VisualTreeAttachmentEventArgs e)
    {
        base.OnDetachedFromVisualTree(e);

        if (DataContext is WaitStaffViewModel vm)
        {
            vm.TriggerNotif -= MainWindow.NotificationMessage;
        }
    }
}
