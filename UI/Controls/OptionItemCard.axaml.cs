using Avalonia;
using Avalonia.Controls;
using Pos.Models;

namespace Pos.Controls;

public partial class OptionItemCard : UserControl
{
    public static readonly StyledProperty<OptionItem> OptionItemProperty =
    AvaloniaProperty.Register<OptionItemCard, OptionItem>(nameof(OptionItem));

    public OptionItem OptionItem {
        get => GetValue(OptionItemProperty);
        set => SetValue(OptionItemProperty, value);
    }
    public OptionItemCard()
    {
        InitializeComponent();
    }
}