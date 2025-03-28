using Avalonia;
using Avalonia.Controls;
using Avalonia.Media;
using Pos.Models;
using Avalonia.Controls.Templates;
using Avalonia.Layout;
using Pos.Controls;
using System.Collections.Generic;
using System.Linq;

namespace Pos.Pages.Home.Views.Options;

public partial class Options : UserControl
{
    private const int TabItemsPerRow = 5;

    public Options()
    {
        InitializeComponent();
        DataContext = new OptionsViewModel();
        var viewModel = DataContext as OptionsViewModel;
        
        if (viewModel == null) return;
        
        foreach (var (optionGroup, index) in viewModel.OptionGroups.Select((group, i) => (group, i)))
        {
            var tabItem = CreateTabItem(optionGroup, viewModel.Options, index, viewModel.OptionGroups.Count);
            viewModel.Tabs.Add(tabItem);
        }
    }

    private static TabItem CreateTabItem(OptionGroup optionGroup, IEnumerable<OptionItem> options, int index, int totalCount)
    {
        var contentContainer = new StackPanel();
        contentContainer.Children.Add(CreateTitleLabel("Options"));
        contentContainer.Children.Add(CreateItemsRepeater(options));
        
        var tabItem = new TabItem
        {
            Header = new TextBlock { Text = optionGroup.Name, TextAlignment = TextAlignment.Center },
            Content = contentContainer,
            BorderBrush = new SolidColorBrush(Color.Parse("#2b2b2b")),
            CornerRadius = DetermineCornerRadius(index, totalCount),
            BorderThickness = DetermineBorderThickness(index)
        };
        
        return tabItem;
    }

    private static TextBlock CreateTitleLabel(string text) => new()
    {
        Text = text,
        Foreground = new SolidColorBrush(Color.Parse("#FFE9C1")),
        FontSize = 24,
        Margin = new Thickness(0, 30, 0, 20)
    };

    private static ItemsRepeater CreateItemsRepeater(IEnumerable<OptionItem> items) => new()
    {
        ItemsSource = items,
        VerticalAlignment = VerticalAlignment.Top,
        Margin = new Thickness(0, 0, 0, 20),
        Layout = new UniformGridLayout
        {
            MinItemWidth = 160,
            MinItemHeight = 100,
            ItemsStretch = UniformGridLayoutItemsStretch.Uniform,
            MinRowSpacing = 16,
            MinColumnSpacing = 16
        },
        ItemTemplate = new FuncDataTemplate<OptionItem>((option, _) =>
        {
            return new OptionItemCard { DataContext = option };
        })
    };
    
    private static CornerRadius DetermineCornerRadius(int index, int totalCount)
    {
        var remainder = (index + 1) % TabItemsPerRow;
        return remainder switch
        {
            1 => new CornerRadius(10, 0, 0, 10),
            0 => new CornerRadius(0, 10, 10, 0),
            _ when index == totalCount - 1 => new CornerRadius(0, 10, 10, 0),
            _ => new CornerRadius(0)
        };
    }
    
    private static Thickness DetermineBorderThickness(int index) =>
        (index + 1) % TabItemsPerRow != 1 ? new Thickness(1, 0, 0, 0) : new Thickness(0);
}
