using System;
using System.IO;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Platform.Storage;
using Avalonia.ReactiveUI;
using Avalonia.Threading;
using System.Linq;
using Avalonia.Input;
using Avalonia.Markup.Xaml.MarkupExtensions;

namespace Pos.Pages.Products;

public partial class CreateProductView : ReactiveUserControl<CreateProductViewModel>
{
    public CreateProductView()
    {
        InitializeComponent();

        Dispatcher.UIThread.Post(() =>
        {
            ViewModel.TriggerNotif += MainWindow.NotificationMessage;
            ViewModel.LoadCategoryCommand.Execute().Subscribe();
        });

        PriceTextBox.AddHandler(TextInputEvent, PriceTextBox_TextInput, RoutingStrategies.Tunnel);
    }

    public async void NextButton_Click(object sender, RoutedEventArgs args)
    {
        NextButton.IsEnabled = false;
        var success = await ViewModel!.AddOrEditProductAsync();
        if (success)
        {
            ViewModel.GoBack.Execute();
        }
        NextButton.IsEnabled = true;
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

    private void DescriptionBox_TextChanged(object? sender, TextChangedEventArgs e)
    {
        var currentLength = DescriptionName.Text?.Length ?? 0;
        DescriptionCounter.Text = $"{currentLength} / 500 Characters";
    }

    private void ProductBox_TextChanged(object? sender, TextChangedEventArgs e)
    {
        var currentLength = ProductName.Text?.Length ?? 0;
        ProductCounter.Text = $"{currentLength} / 250 Characters";
    }

    private void OnButtonFlyoutOpened(object sender, EventArgs e)
    {
        CategoryDropdownIcon.Bind(PathIcon.DataProperty, new DynamicResourceExtension("SemiIconChevronRight"));
    }

    private void OnButtonFlyoutClosed(object sender, EventArgs e)
    {
        CategoryDropdownIcon.Bind(PathIcon.DataProperty, new DynamicResourceExtension("SemiIconChevronDown"));
    }
}