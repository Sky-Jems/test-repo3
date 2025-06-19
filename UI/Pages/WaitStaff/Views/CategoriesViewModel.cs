using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Threading.Tasks;
using System.Windows.Input;
using pos.Api;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.WaitStaff;

public partial class CategoriesViewModel : ReactiveObject, IRoutableViewModel
{
    public string? UrlPathSegment => throw new NotImplementedException();
    public IScreen HostScreen { get; }
    public ICommand CategoryCardClickedCommand { get; }
    private readonly ICategoryService _categoryService;
    private readonly IProductService _productService;
    public ObservableCollection<Category> Categories { get; } = new ObservableCollection<Category>();
    public ReactiveCommand<Unit, Unit> LoadCategoriesCommand { get; }

    public CategoriesViewModel(IScreen screen, ICategoryService categoryService, IProductService productService)
    {
        this.HostScreen = screen;
        this.CategoryCardClickedCommand = ReactiveCommand.Create<Category>(HandleClickCategory);

        this._categoryService = categoryService;
        this._productService = productService;
        LoadCategoriesCommand = ReactiveCommand.CreateFromTask(LoadCategoriesAsync);
        LoadCategoriesCommand.Execute().Subscribe();
    }

    private async Task LoadCategoriesAsync()
    {
        try
        {
            Console.WriteLine($"Loading categories...{_categoryService}");
            var categories = await _categoryService.GetAllCategoriesAsync();
            if (categories.Any())
            {
                Categories.Clear();
                foreach (var category in categories)
                {
                    Categories.Add(category);
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
    }

    public void HandleClickCategory(Category item)
    {
        this.HostScreen.Router.Navigate.Execute(new MenuViewModel(this.HostScreen, item, _productService));
    }
}