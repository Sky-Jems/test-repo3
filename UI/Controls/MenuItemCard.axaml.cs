using System.Windows.Input;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Pos.Models;

namespace Pos.Controls;

public partial class MenuItemCard : UserControl
{
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
    }

    public static readonly StyledProperty<ICommand> MenuItemCommandProperty =
        AvaloniaProperty.Register<MenuItemCard, ICommand>(nameof(MenuItemCommand));

    public ICommand MenuItemCommand
    {
        get => GetValue(MenuItemCommandProperty);
        set => SetValue(MenuItemCommandProperty, value);
    }

    private void OnButtonClick(object? sender, RoutedEventArgs e)
    {
        if (MenuItemCommand?.CanExecute(Product) == true)
        {
            MenuItemCommand.Execute(Product);
        }
    }
}