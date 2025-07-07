using Avalonia;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Interactivity;
using Avalonia.Markup.Xaml;
using Avalonia.VisualTree;
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

    private void TextBox_GotFocus(object? sender, GotFocusEventArgs e)
    {
        if (sender is TextBox textBox)
        {
            textBox.CaretIndex = textBox.Text?.Length ?? 0;
        }

    }
    private void CloseButton_Click(object? sender, RoutedEventArgs e)
    {
        Close(string.Empty);
    }
}