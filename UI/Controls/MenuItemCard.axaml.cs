using Avalonia;
using Avalonia.Controls;

namespace Pos.Controls;

public partial class MenuItemCard : UserControl
{
    public static readonly StyledProperty<Models.MenuItem> MenuItemProperty =
        AvaloniaProperty.Register<MenuItemCard, Models.MenuItem>(nameof(MenuItem));

    public Models.MenuItem MenuItem {
        get => GetValue(MenuItemProperty);
        set => SetValue(MenuItemProperty, value);
    }

    public MenuItemCard()
    {
        InitializeComponent();
    }
}