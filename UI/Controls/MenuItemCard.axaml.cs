using System.Diagnostics;
using System.Windows.Input;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Input;
using pos.Dialogs;
using Pos.Dialogs;
using Pos.Models;

namespace Pos.Controls;

public partial class MenuItemCard : UserControl
{
    private readonly Stopwatch timer = new Stopwatch();
    public static readonly StyledProperty<Product> ProductProperty =
        AvaloniaProperty.Register<MenuItemCard, Product>(nameof(Product));

    public Product Product
    {
        get => GetValue(ProductProperty);
        set => SetValue(ProductProperty, value);
    }

    public static readonly StyledProperty<Models.MenuItem> MenuItemProperty =
        AvaloniaProperty.Register<MenuItemCard, Models.MenuItem>(nameof(MenuItem));

    public Models.MenuItem MenuItem
    {
        get => GetValue(MenuItemProperty);
        set => SetValue(MenuItemProperty, value);
    }

    public MenuItemCard()
    {
        InitializeComponent();
        productCard.AddHandler(Button.PointerPressedEvent, OnPointerPressed, handledEventsToo: true);
        productCard.AddHandler(Button.PointerReleasedEvent, OnPointerReleased, handledEventsToo: true);
    }

    public static readonly StyledProperty<ICommand> MenuItemCommandProperty =
        AvaloniaProperty.Register<MenuItemCard, ICommand>(nameof(MenuItemCommand));

    public ICommand MenuItemCommand
    {
        get => GetValue(MenuItemCommandProperty);
        set => SetValue(MenuItemCommandProperty, value);
    }

    private void OnPointerPressed(object? sender, PointerPressedEventArgs e)
    {
        timer.Restart();
    }

    private async void OnPointerReleased(object? sender, PointerReleasedEventArgs e)
    {
        timer.Stop();

        if (MenuItemCommand?.CanExecute(Product) == true && timer.ElapsedMilliseconds < 200)
        {
            MenuItemCommand.Execute(Product);
        }
        else
        {
            DetailedProductDialog dialog = new();
            (dialog.DataContext as DetailedProductDialogViewModel).Name = Product.Name;
            (dialog.DataContext as DetailedProductDialogViewModel).Description = Product.Description;
            (dialog.DataContext as DetailedProductDialogViewModel).Price = Product.Price;
            await dialog.ShowAsync();
        }
    }
}