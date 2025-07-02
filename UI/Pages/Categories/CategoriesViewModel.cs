using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using pos.Models.EventArgs;
using Pos.Util;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.Categories;

public class CategoriesViewModel : ReactiveObject
{
    public event EventHandler<NotificationEventArgs> TriggerNotif;
    private ICategoryService _categoryService;

    public ObservableCollection<Category> Categories { get; } = new();
    private List<Category> _categories = new();

    public ReactiveCommand<Unit, Unit> LoadCategoriesCommand { get; }
    public ReactiveCommand<Unit, Unit> CreateCategoryCommand { get; }
    public ReactiveCommand<Unit, Unit> EditCategoryCommand { get; }
    public ReactiveCommand<Unit, Unit> DeleteCategoryCommand { get; }

    private string _searchText = "";
    public string SearchText
    {
        get => _searchText;
        set
        {
            this.RaiseAndSetIfChanged(ref _searchText, value);
            FilterCategories();
        }
    }

    public CategoriesViewModel()
    {
        _categoryService = ServiceLocator.Services.GetRequiredService<ICategoryService>();
        Categories = new ObservableCollection<Category>();
        LoadCategoriesCommand = ReactiveCommand.CreateFromTask(FetchCategories);
        LoadCategoriesCommand.Execute();
    }

    private void FilterCategories()
    {
        Categories.Clear();
        IEnumerable<Category> filtered;
        filtered = string.IsNullOrWhiteSpace(SearchText)
            ? _categories
            : _categories.Where(p => p.Name?.Contains(SearchText, StringComparison.OrdinalIgnoreCase) == true);
        foreach (var category in filtered)
        {
            Categories.Add(category);
        }
    }

    private async Task FetchCategories()
    {
        try
        {
            var categories = await _categoryService.GetAllCategoriesAsync();
            if (categories.Any())
            {
                Categories.Clear();
                _categories = categories.ToList();
                foreach (var category in _categories)
                {
                    Categories.Add(category);
                }
            }
        }
        catch (Exception)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Error("Loading of categories failed."));
        }
    }

    public async Task CreateCategory(string name)
    {
        try
        {
            Category category = new Category();
            category.Id = null;
            category.Name = name;
            await _categoryService.CreateCategory(category);
            LoadCategoriesCommand.Execute();
        }
        catch (Exception)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Error("Category create failed."));
        }
    }

    public async Task UpdateCategory(long id, string name)
    {
        try
        {
            Category category = new Category();
            category.Id = id;
            category.Name = name;
            await _categoryService.UpdateCategory(id, category);
            LoadCategoriesCommand.Execute();
        }
        catch (Exception)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Error("Category update failed."));
        }
    }

    public async Task DeleteCategory(long id)
    {
        try
        {
            await _categoryService.DeleteCategory(id);
            LoadCategoriesCommand.Execute();
        }
        catch (Exception)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Error("Category delete failed."));
        }
    }
}
