using System.Reactive;
using Avalonia;
using Avalonia.Controls;
using ReactiveUI;

namespace Pos.Controls;

public partial class OrderDisplayPanel : UserControl
{
    public static readonly StyledProperty<bool> ShowSummaryButtonProperty =
        AvaloniaProperty.Register<CategoryCard, bool>(nameof(ShowSummaryButton), true);

    public bool ShowSummaryButton
    {
        get => GetValue(ShowSummaryButtonProperty);
        set => SetValue(ShowSummaryButtonProperty, value);
    }

    public static readonly StyledProperty<string> SummaryButtonTextProperty =
        AvaloniaProperty.Register<CategoryCard, string>(nameof(SummaryButtonText));

    public static readonly StyledProperty<ReactiveCommand<Unit, Unit>> SummaryButtonCommandProperty =
        AvaloniaProperty.Register<OrderCartPanel, ReactiveCommand<Unit, Unit>>(nameof(SummaryButtonCommand));

    public ReactiveCommand<Unit, Unit> SummaryButtonCommand
    {
        get => GetValue(SummaryButtonCommandProperty);
        set => SetValue(SummaryButtonCommandProperty, value);
    }

    public string SummaryButtonText
    {
        get => GetValue(SummaryButtonTextProperty);
        set => SetValue(SummaryButtonTextProperty, value);
    }

    public OrderDisplayPanel()
    {
        InitializeComponent();
    }
}