using System.Collections.ObjectModel;
using System.Reactive;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.Home.Views.Menu;

public partial class MenuViewModel : ReactiveObject, IRoutableViewModel
{
    public ObservableCollection<MenuItem> Items { get; set; }
    public string? UrlPathSegment => throw new System.NotImplementedException();
    public IScreen HostScreen { get; }
    public Category Category { get; }

    public MenuViewModel(IScreen screen, Category category)
    {
        this.HostScreen = screen;
        this.Category = category;

        Items =
        [
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100),
            new(1, "Baked Shrimp", 100)
        ];
    }

    public ReactiveCommand<Unit, IRoutableViewModel> GoBack => this.HostScreen.Router.NavigateBack;
}