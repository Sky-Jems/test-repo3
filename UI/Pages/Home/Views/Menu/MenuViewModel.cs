using System.Collections.ObjectModel;
using System.Reactive;
using System.Windows.Input;
using Pos.Models;
using Pos.Pages.Home.Views.Options;
using ReactiveUI;

namespace Pos.Pages.Home.Views.Menu;

public partial class MenuViewModel : ReactiveObject, IRoutableViewModel
{
    public ObservableCollection<MenuItem> Items { get; set; }
    public string? UrlPathSegment => throw new System.NotImplementedException();
    public IScreen HostScreen { get; }
    public Category Category { get; }
    public ICommand MenuItemClickedCommand { get; }

    public MenuViewModel(IScreen screen, Category category)
    {
        this.HostScreen = screen;
        this.Category = category;
        this.MenuItemClickedCommand = ReactiveCommand.Create<MenuItem>(HandleClickMenuItem);

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

    public void HandleClickMenuItem(MenuItem menuItem)
    {
        this.HostScreen.Router.Navigate.Execute(new OptionsViewModel(this.HostScreen, menuItem));
    }
}