using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using Pos.Contants;
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
    public ReactiveCommand<Unit, Unit> LoadCategoriesCommand { get; }
    public ICommand CategoryCardClickedCommand { get; }

    public CategoriesViewModel(IScreen screen)
    {
        HostScreen = screen;
        _categoryService = ServiceLocator.Services.GetRequiredService<ICategoryService>();

        LoadCategoriesCommand = ReactiveCommand.CreateFromTask(LoadCategoriesAsync);
        CategoryCardClickedCommand = ReactiveCommand.Create<Category>(HandleClickCategory);
    }

    private async Task LoadCategoriesAsync()
    {
        try
        {
            List<string> colors = [Classes.SUCCESS, Classes.PRIMARY, Classes.SECONDARY, Classes.WARNING, Classes.DANGER];
            List<Category> categories = await _categoryService.GetAllCategoriesAsync();
            Categories.Clear();
            for (int x = 0; x < categories.Count; x++)
            {
                categories[x].ButtonVariant = colors[x % colors.Count];
                Categories.Add(categories[x]);
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
