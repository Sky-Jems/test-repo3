using Avalonia;
using Avalonia.Controls;
using Avalonia.ReactiveUI;
using System;
using Avalonia.Interactivity;
using Avalonia.Threading;
using Pos.Models;
using ReactiveUI;
using System.Reactive.Disposables;
using System.Reactive.Linq;
using Pos.Dialogs;

namespace Pos.Pages.Products;

public partial class MainView : ReactiveUserControl<MainViewModel>
{
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
            ViewModel.TriggerNotif -= MainWindow.NotificationMessage;
            ViewModel.TriggerNotif += MainWindow.NotificationMessage;
        });
    }

    private async void DeleteButton_Click(object sender, RoutedEventArgs e)
    {
        ConfirmationDialog dialog = new()
        {
            Title = "Delete this product?",
            Message = "Are you sure you want to delete this product?",
            PositiveText = "Delete",
            NegativeText = "Cancel"
        };
        dialog.FindControl<Button>("PositiveButton")!.Classes.Add("Danger");
        if ((await dialog.ShowAsync()).GetValueOrDefault(false))
        {
            await ViewModel?.DeleteProductById(Convert.ToInt32(((Button)sender).Tag));
        }
    }

    private void EditButton_Click(object sender, RoutedEventArgs e)
    {
        Product product = (Product)((Button)sender).Tag;
        var createVM = new CreateProductViewModel(ViewModel.HostScreen, ViewModel._productService, ViewModel._categoryService);
        createVM.SetProductForEditAsync((long)product.Id);
        ViewModel.HostScreen.Router.Navigate.Execute(createVM);
    }

    protected override void OnAttachedToVisualTree(VisualTreeAttachmentEventArgs e)
    {
        base.OnAttachedToVisualTree(e);
        ViewModel.LoadProductList();
    }
}