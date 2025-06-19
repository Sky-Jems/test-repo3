using System;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Pos.Models;

namespace Pos.Controls;

public partial class OptionItemCard : UserControl
{
    public event EventHandler<Variant>? VariantClicked;
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
    
    private void OnButtonClick(object? sender, RoutedEventArgs e)
    {
        if (DataContext is Variant variant)
        {
            VariantClicked?.Invoke(this, variant);
        }
    }
}