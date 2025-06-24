using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Threading.Tasks;
using System.Windows.Input;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.WaitStaff;

public class CategoriesViewModel : ReactiveObject, IRoutableViewModel
{
    public string? UrlPathSegment => throw new NotImplementedException();
    public IScreen HostScreen { get; }
    private readonly ICategoryService _categoryService;
    public ObservableCollection<Category> Categories { get; } = new ();
    private ReactiveCommand<Unit, Unit> LoadCategoriesCommand { get; }
    public ICommand CategoryCardClickedCommand { get; }

    public CategoriesViewModel(IScreen screen)
    {
        HostScreen = screen;
        _categoryService = ServiceLocator.Services.GetRequiredService<ICategoryService>();

        LoadCategoriesCommand = ReactiveCommand.CreateFromTask(LoadCategoriesAsync);
        CategoryCardClickedCommand = ReactiveCommand.Create<Category>(HandleClickCategory);
        LoadCategoriesCommand.Execute().Subscribe();
    }

    private async Task LoadCategoriesAsync()
    {
        try
        {
            var categories = await _categoryService.GetAllCategoriesAsync();
            Categories.Clear();
            foreach (var category in categories)
            {
                Categories.Add(category);
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
    }

    private void HandleClickCategory(Category category)
    {
        HostScreen.Router.Navigate.Execute(new MenuViewModel(HostScreen, category));
    }
}
