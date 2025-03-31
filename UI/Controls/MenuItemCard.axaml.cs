using System.Windows.Input;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Pos.Controls;

public partial class MenuItemCard : UserControl
{
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
        if (MenuItemCommand?.CanExecute(MenuItem) == true)
        {
            MenuItemCommand.Execute(MenuItem);
        }
    }
}