using System;
using System.Linq;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Media;
using Avalonia.ReactiveUI;
using AvaloniaDialogs.Views;
using Pos.Dialogs;
using Pos.Models;
using Pos.Pages.Categories.Dialogs;

namespace Pos.Pages.Categories;

public partial class CategoriesView : ReactiveUserControl<CategoriesViewModel>
{

    private CategoriesViewModel viewModel;
    public CategoriesView()
    {
        viewModel = new CategoriesViewModel();
        DataContext = viewModel;
        ViewModel!.TriggerNotif += MainWindow.NotificationMessage;
        InitializeComponent();
    }

    private async void AddNewItemButton_Click(object sender, RoutedEventArgs args)
    {
        CategoryDialog dialog = new("Create Category", null);
        string categoryName = (await dialog.ShowAsync()).GetValueOrDefault();
        if (!string.IsNullOrEmpty(categoryName))
        {
            await viewModel.CreateCategory(categoryName);
        }
    }

    private async void UpdateButton_Click(object sender, RoutedEventArgs args)
    {
        long id = Convert.ToInt32(((Button)sender).Tag);
        Category category = viewModel.Categories.Single(cat => cat.Id == id);
        CategoryDialog dialog = new("Update Category", category.Name);
        string categoryName = (await dialog.ShowAsync()).GetValueOrDefault();
        if (!string.IsNullOrEmpty(categoryName))
        {
            await viewModel.UpdateCategory(id, categoryName);
        }
    }

    private async void DeleteButton_Click(object sender, RoutedEventArgs args)
    {
        ConfirmationDialog dialog = new()
        {
            Title = "Delete category?",
            Message = "Deleting this category will also affect the products it contains.",
            PositiveText = "Delete",
            NegativeText = "Cancel"
        };
        dialog.FindControl<Button>("PositiveButton")!.Classes.Add("Danger");
        if ((await dialog.ShowAsync()).GetValueOrDefault(false))
        {
            long id = Convert.ToInt32(((Button)sender).Tag);
            await viewModel.DeleteCategory(id);
        }
    }
}