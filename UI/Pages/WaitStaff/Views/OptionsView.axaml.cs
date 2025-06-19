using Avalonia;
using Avalonia.Controls;
using Avalonia.Media;
using Pos.Models;
using Avalonia.Controls.Templates;
using Avalonia.Layout;
using Pos.Controls;
using System.Collections.Generic;
using System.Linq;
using Avalonia.ReactiveUI;
using System;
using Avalonia.Threading;

namespace Pos.Pages.WaitStaff;

public partial class OptionsView : ReactiveUserControl<OptionsViewModel>
{
    private int _selectedTabIndex = -1;
    private const int TabItemsPerRow = 5;

    public OptionsView()
    {
        InitializeComponent();
    }

    protected override void OnAttachedToVisualTree(VisualTreeAttachmentEventArgs e)
    {
        base.OnAttachedToVisualTree(e);
        ViewModel.LoadOptionGroup += VariantOptionsChanged;
        ViewModel.LoadOptionGroupValues += VariantOptionValuesChanged;
        ViewModel.ReloadOptionGroup();
    }

    private void VariantOptionsChanged(object? sender, EventArgs e)
    {
        Dispatcher.UIThread.Post(() =>
        {
            ViewModel.Tabs.Clear();
            foreach (var (optionGroup, index) in ViewModel.OptionGroups.Select((group, i) => (group, i)))
            {
                var tabItem = CreateTabItem(optionGroup);
                ViewModel.Tabs.Add(tabItem);
            }
            ViewModel.ReloadVariants();
        });
    }

    private void VariantOptionValuesChanged(object? sender, EventArgs e)
    {
        if (_selectedTabIndex < 0) return;
        StackPanel contentContainer = CreateStackPanel(ViewModel.Variants);
        ViewModel.Tabs[_selectedTabIndex].Content = contentContainer;
    }

    private void SelectedVariantOptionsChanged(object? sender, SelectionChangedEventArgs e)
    {
        if (sender is TabControl tabControl && tabControl.SelectedItem is TabItem tabItem)
        {
            Dispatcher.UIThread.Post(() =>
            {
                if (tabControl.SelectedIndex < 0) return;
                _selectedTabIndex = tabControl.SelectedIndex;
                // ViewModel.selectedVariantId = (int)tabItem.Tag;
                ViewModel.ReloadVariants();
            });
        }
    }

    private TabItem CreateTabItem(OptionGroup optionGroup)
    {
        var tabItem = new TabItem
        {
            Header = new TextBlock { Text = optionGroup.Name, TextAlignment = TextAlignment.Center },
            Content = null,
            IsVisible = true,
            Tag = optionGroup.Id
        };
        return tabItem;
    }

    private StackPanel CreateStackPanel(IEnumerable<Variant> variants)
    {
        StackPanel contentContainer = new StackPanel();
        contentContainer.Children.Add(CreateTitleLabel("Variants"));
        contentContainer.Children.Add(CreateItemsRepeater(ViewModel));
        return contentContainer;
    }

    private static TextBlock CreateTitleLabel(string text) => new()
    {
        Text = text,
        FontSize = 24,
        Margin = new Thickness(0, 30, 0, 20)
    };

    private static ItemsRepeater CreateItemsRepeater(OptionsViewModel vm) => new()
    {
        ItemsSource = vm.Variants,
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
        ItemTemplate = new FuncDataTemplate<Variant>((variant, _) =>
        {
            var card = new OptionItemCard { DataContext = variant };
            // Subscribe to the event and invoke the command
            card.VariantClicked += (s, v) =>
            {
                if (vm.ClickVariantCommand.CanExecute(v))
                    vm.ClickVariantCommand.Execute(v);
            };

            return card;
        })
    };

    private static Thickness DetermineBorderThickness(int index) =>
        (index + 1) % TabItemsPerRow != 1 ? new Thickness(1, 0, 0, 0) : new Thickness(0);

}