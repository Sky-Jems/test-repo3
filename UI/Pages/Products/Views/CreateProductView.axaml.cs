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
using Pos.Models;

namespace Pos.Pages.Products;

public partial class CreateProductView : ReactiveUserControl<CreateProductViewModel>
{
    public CreateProductView()
    {
        InitializeComponent();

        Dispatcher.UIThread.Post(() =>
        {
            ViewModel!.TriggerNotif += MainWindow.NotificationMessage;
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

    private void OnButtonFlyoutOpened(object sender, EventArgs e)
    {
        CategoryDropdownIcon.Bind(PathIcon.DataProperty, new DynamicResourceExtension("SemiIconChevronRight"));
        SelectedCategoryListBox.SelectedItems!.Clear();
        foreach (var selectedCategory in ViewModel!.SelectedCategoryList)
        {
            SelectedCategoryListBox.SelectedItems.Add(ViewModel!.CategoryList.First(x => x.Id == selectedCategory.Id));
        }
    }

    private void OnButtonFlyoutClosed(object sender, EventArgs e)
    {
        CategoryDropdownIcon.Bind(PathIcon.DataProperty, new DynamicResourceExtension("SemiIconChevronDown"));
    }

    private void OnCategorySelectionChanged(object? sender, SelectionChangedEventArgs e)
    {
        if (DataContext is CreateProductViewModel vm && sender is ListBox listBox)
        {
            vm.SetCategoryList(listBox.SelectedItems.OfType<Category>());

        }
    }

}