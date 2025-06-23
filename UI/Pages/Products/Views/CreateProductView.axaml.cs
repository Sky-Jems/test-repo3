using System;
using Avalonia;
using System.IO;
using Avalonia.Controls;
using Avalonia.Controls.Notifications;
using Avalonia.Interactivity;
using Avalonia.Platform.Storage;
using Avalonia.ReactiveUI;
using Avalonia.Threading;
using pos.Models.EventArgs;
using System.Linq;
using Avalonia.Input;

namespace Pos.Pages.Products;

public partial class CreateProductView : ReactiveUserControl<CreateProductViewModel>
{
    private WindowNotificationManager? _manager;

    public CreateProductView()
    {
        InitializeComponent();

        Dispatcher.UIThread.Post(() =>
        {
            ViewModel.TriggerNotif += NotificationMessage;
            ViewModel.LoadCategoryCommand.Execute().Subscribe();
        });

        PriceTextBox.AddHandler(TextInputEvent, PriceTextBox_TextInput, RoutingStrategies.Tunnel);
    }

    protected override void OnAttachedToVisualTree(VisualTreeAttachmentEventArgs e)
    {
        base.OnAttachedToVisualTree(e);
        var topLevel = TopLevel.GetTopLevel(this);
        _manager = new WindowNotificationManager(topLevel) { MaxItems = 3 };
    }

    public async void NextButton_Click(object sender, RoutedEventArgs args)
    {
        var success = await ViewModel!.AddOrEditProductAsync();
        if (success)
        {
            ViewModel.GoBack.Execute();
        }
    }

    private void PriceTextBox_TextInput(object? sender, TextInputEventArgs e)
    {
        if (!e.Text.All(c => char.IsDigit(c) || c == '.'))
        {
            e.Handled = true;
        }
        else if (e.Text == "." && ((sender as TextBox)?.Text.Contains(".") ?? false))
        {
            e.Handled = true;
        }
    }
    private async void UploadImageButton_Clicked(object sender, RoutedEventArgs args)
    {
        var topLevel = TopLevel.GetTopLevel(this);

        var files = await topLevel.StorageProvider.OpenFilePickerAsync(new FilePickerOpenOptions
        {
            Title = "Upload an image",
            AllowMultiple = false,
            FileTypeFilter = [FilePickerFileTypes.ImageAll]
        });

        if (files.Count >= 1)
        {
            await using var stream = await files[0].OpenReadAsync();
            using var streamReader = new StreamReader(stream);
            var fileContent = await streamReader.ReadToEndAsync();
        }
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
}