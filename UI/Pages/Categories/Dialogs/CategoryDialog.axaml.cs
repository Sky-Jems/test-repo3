using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Markup.Xaml;
using AvaloniaDialogs.Views;

namespace Pos.Pages.Categories.Dialogs;


public partial class CategoryDialog : BaseDialog<string>
{
    public CategoryDialog(string label, string? name)
    {
        InitializeComponent();
        DialogTitle.Content = label;
        categoryTextBox.Text = name;
    }


    private async void SaveButton_Click(object sender, RoutedEventArgs args)
    {
        Close(categoryTextBox.Text);
    }
}