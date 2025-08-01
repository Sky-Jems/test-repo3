using System;
using System.Diagnostics;
using System.Windows.Input;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Interactivity;
using pos.Dialogs;
using Pos.Dialogs;
using Pos.Models;

namespace Pos.Controls;

public partial class CategoryCard : UserControl
{
    private readonly Stopwatch timer = new Stopwatch();
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
        categoryCard.AddHandler(Button.PointerPressedEvent, OnPointerPressed, handledEventsToo: true);
        categoryCard.AddHandler(Button.PointerReleasedEvent, OnPointerReleased, handledEventsToo: true);
        this.DataContextChanged += OnDataContextChanged;
    }

    private void OnButtonClick(object? sender, RoutedEventArgs e)
    {
        if (CategoryCommand?.CanExecute(Category) == true)
        {
            CategoryCommand.Execute(Category);
        }
    }

    private void OnPointerPressed(object? sender, PointerPressedEventArgs e)
    {
        timer.Restart();
    }

    private async void OnPointerReleased(object? sender, PointerReleasedEventArgs e)
    {
        timer.Stop();

        if (CategoryCommand?.CanExecute(Category) == true && timer.ElapsedMilliseconds < 200)
        {
            CategoryCommand.Execute(Category);
        }
        else
        {
            DetailedCategoryDialog dialog = new();
            (dialog.DataContext as DetailedCategoryDialogViewModel).Name = Category.Name;
            await dialog.ShowAsync();
        }
    }

    private void OnDataContextChanged(object? sender, EventArgs e)
    {
        if (DataContext is Category category)
        {
            categoryCard.Classes.Add(category.ButtonVariant);
        }
    }
}