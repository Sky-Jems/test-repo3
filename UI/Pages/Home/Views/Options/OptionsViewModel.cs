using System;
using System.Collections.ObjectModel;
using System.Reactive;
using Avalonia.Controls;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.Home.Views.Options;

public partial class OptionsViewModel : ReactiveObject, IRoutableViewModel
{
    public string? UrlPathSegment => throw new System.NotImplementedException();
    public IScreen HostScreen { get; }
    public Models.MenuItem MenuItem { get; set; }
    public ObservableCollection<OptionItem> Options { get; set; }
    public ObservableCollection<OptionGroup> OptionGroups { get; set; }
    public ObservableCollection<TabItem> Tabs { get; set; } = new();
    public OptionsViewModel(IScreen screen, Models.MenuItem menuItem)
    {
        this.HostScreen = screen;
        this.MenuItem = menuItem;
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
    public ReactiveCommand<Unit, IRoutableViewModel> GoBack => this.HostScreen.Router.NavigateBack;
}