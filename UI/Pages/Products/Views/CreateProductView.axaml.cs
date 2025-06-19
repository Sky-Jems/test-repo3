using System;
using Avalonia;
using System.IO;
using Avalonia.Controls;
using Avalonia.Controls.Notifications;
using Avalonia.Input;
using Avalonia.Interactivity;
using Avalonia.Platform.Storage;
using Avalonia.ReactiveUI;
using Avalonia.Threading;
using pos.Models.EventArgs;
using Pos.Controls;
using Pos.Models;
using System.Collections.Generic;
using Pos.Pages.Cms.Tabs.Products.Constants;

namespace Pos.Pages.Products;

public partial class CreateProductView : ReactiveUserControl<CreateProductViewModel>
{
    private WindowNotificationManager? _manager;
    private List<string> breadcrumbTitle = new List<string>
    {
        ProductConstants.Details,
        ProductConstants.Variants,
        ProductConstants.Combinations
    };

    public CreateProductView()
    {
        InitializeComponent();
        Breadcrumb breadcrumb = new()
        {
            Content = breadcrumbTitle[0]
        };
        breadcrumbNavigationPanel.Children.Add(breadcrumb);

        Dispatcher.UIThread.Post(() =>
        {
            ViewModel.TriggerNotif += NotificationMessage;
            ViewModel.LoadCategoryCommand.Execute().Subscribe();
        });
        VariantGroupComboBox.SelectionChanged += (s, e) =>
        {
            if (DataContext is CreateProductViewModel vm)
            {
                vm.OnSelectedVariantGroupChanged();
            }
        };
    }

    protected override void OnAttachedToVisualTree(VisualTreeAttachmentEventArgs e)
    {
        base.OnAttachedToVisualTree(e);
        var topLevel = TopLevel.GetTopLevel(this);
        _manager = new WindowNotificationManager(topLevel) { MaxItems = 3 };
    }

    public void NextButton_Click(object sender, RoutedEventArgs args)
    {
        if (createProductPagination.SelectedIndex == breadcrumbTitle.IndexOf(ProductConstants.Details))
        {
            ViewModel?.AddOrEditProductAsync();
        }
        else if (createProductPagination.SelectedIndex == breadcrumbTitle.IndexOf(ProductConstants.Variants))
        {
            if (ViewModel?.SaveVariantGroupsCommand is not null)
            {
                ViewModel.SaveVariantGroupsCommand.Execute();
            }
        }
        else if (createProductPagination.SelectedIndex == breadcrumbTitle.IndexOf(ProductConstants.Combinations))
        {
            ViewModel.GoBack.Execute();
            return;
        }

        int currentIndex = ++createProductPagination.SelectedIndex;
        Breadcrumb breadcrumb = new()
        {
            Content = breadcrumbTitle[currentIndex],
        };
        breadcrumbNavigationPanel.Children.Add(breadcrumb);
        createProductPagination.SelectedIndex = currentIndex;
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
    private void OnDeleteVariantGroupClick(object? sender, RoutedEventArgs e)
    {
        if (DataContext is CreateProductViewModel vm && sender is Button btn && btn.Tag is OptionGroup group)
        {
            vm.DeleteVariantGroupCommand.Execute(group).Subscribe();
        }
    }

    private void CreateProductPagination_SelectionChanged(object sender, SelectionChangedEventArgs args)
    {
        int? currentIndex = createProductPagination?.SelectedIndex;
        Dispatcher.UIThread.Post(() =>
        {
            NextButton.Content = currentIndex == createProductPagination.Items.Count - 1 ? "SUBMIT" : "NEXT";
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

    private void InsideTextBoxKeyDown(object? sender, KeyEventArgs e)
    {
        if (e.Key == Key.Enter && sender is TextBox txtBox)
        {
            ViewModel.ProductOptions[(int)txtBox.Tag].Values.Add(new OptionItem { Id = null, Value = txtBox.Text });
            txtBox.Clear();
        }
    }

    private void VariantCombinationsCellUpdate(object? sender, DataGridCellEditEndedEventArgs e)
    {
        if (e.EditAction == DataGridEditAction.Commit)
        {
            Combinations editedItem = (Combinations)e.Row.DataContext;
            ViewModel.UpdateVariantCombination(editedItem);
        }
    }
}