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
    
    public static readonly StyledProperty<Category> SubCategoryProperty =
        AvaloniaProperty.Register<CategoryCard,Category>(nameof(SubCategory));

    public Category SubCategory
    {
        get => GetValue(SubCategoryProperty);
        set => SetValue(SubCategoryProperty, value);
    }

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
    
    public static readonly StyledProperty<ICommand> SubCategoryCommandProperty =
        AvaloniaProperty.Register<CategoryCard, ICommand>(nameof(SubCategoryCommand));

    public ICommand SubCategoryCommand
    {
        get => GetValue(SubCategoryCommandProperty);
        set => SetValue(SubCategoryCommandProperty, value);
    }

    public CategoryCard()
    {
        InitializeComponent();
    }

    private void OnButtonClick(object? sender, RoutedEventArgs e)
    {
        if (SubCategoryCommand?.CanExecute(SubCategory) == true)
        {
            SubCategoryCommand.Execute(SubCategory);
        }
    }
}