using Avalonia;
using Avalonia.Controls;
using Avalonia.ReactiveUI;
using Avalonia.Controls.Notifications;
using System;
using Avalonia.Interactivity;
using pos.Models.EventArgs;
using Avalonia.Threading;
using Pos.Models;
using ReactiveUI;
using System.Reactive.Disposables;
using System.Reactive.Linq;

namespace Pos.Pages.Products;

public partial class MainView : ReactiveUserControl<MainViewModel>
{
    private WindowNotificationManager? _manager;
    public MainView()
    {
        InitializeComponent();
        this.WhenActivated(disposables =>
            {
                this.WhenAnyValue(x => x.ViewModel.SearchText)
                    .Subscribe(_ => ViewModel.FilterProducts())
                    .DisposeWith(disposables);
            });
        Dispatcher.UIThread.Post(() =>
        {
            ViewModel.TriggerNotif += NotificationMessage;
        });
    }

    private async void DeleteButton_Click(object sender, RoutedEventArgs e)
    {
        await ViewModel?.DeleteProductById(Convert.ToInt32(((Button)sender).Tag));
    }

    private void EditButton_Click(object sender, RoutedEventArgs e)
    {
        Product product = (Product)((Button)sender).Tag;
        var createVM = new CreateProductViewModel(ViewModel.HostScreen, ViewModel._productService, ViewModel._categoryService);
        createVM.SetProductForEditAsync((long)product.Id);
        ViewModel.HostScreen.Router.Navigate.Execute(createVM);
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
    protected override void OnAttachedToVisualTree(VisualTreeAttachmentEventArgs e)
    {
        base.OnAttachedToVisualTree(e);
        ViewModel.LoadProductList();
        var topLevel = TopLevel.GetTopLevel(this);
        _manager = new WindowNotificationManager(topLevel) { MaxItems = 3 };
    }
}
