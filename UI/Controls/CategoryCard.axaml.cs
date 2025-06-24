using System;
using System.Windows.Input;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Pos.Models;

namespace Pos.Controls;

public partial class CategoryCard : UserControl
{
    public static readonly StyledProperty<Category> CategoryProperty =
        AvaloniaProperty.Register<CategoryCard, Category>(nameof(Category));

    public Category Category
    {
        get => GetValue(CategoryProperty);
        set => SetValue(CategoryProperty, value);
    }
    
    public static readonly StyledProperty<ICommand> CategoryCommandProperty =
        AvaloniaProperty.Register<CategoryCard, ICommand>(nameof(CategoryCommand));

    public ICommand CategoryCommand
    {
        get => GetValue(CategoryCommandProperty);
        set => SetValue(CategoryCommandProperty, value);
    }

    public CategoryCard()
    {
        InitializeComponent();
    }

    private void OnButtonClick(object? sender, RoutedEventArgs e)
    {
        if (CategoryCommand?.CanExecute(Category) == true)
        {
            CategoryCommand.Execute(Category);
        }
    }
}