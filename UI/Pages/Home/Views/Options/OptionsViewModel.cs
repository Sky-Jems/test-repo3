using System.Collections.ObjectModel;
using Avalonia.Controls;
using Pos.Models;
using Pos.ViewModels;

namespace Pos.Pages.Home.Views.Options;

public partial class OptionsViewModel : ViewModelBase
{
    public ObservableCollection<OptionItem> Options { get; set; }
    public ObservableCollection<OptionGroup> OptionGroups { get; set; }
    public ObservableCollection<TabItem> Tabs { get; set; } = new();
    public OptionsViewModel() {
        Options =
        [
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2),
            new(1, "Garlic", 2)
        ];
        OptionGroups = 
        [
            new(1, "Sizes"),
            new(2, "Sizes"),
            new(3, "Sizes"),
            new(4, "Sizes"),
            new(5, "Sizes"),
            new(6, "Sizes"),
            new(7, "Sizes"),
            new(8, "Sizes"),
        ];
    }
}