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

public partial class MenuViewModel : ReactiveObject, IRoutableViewModel
{
    public ObservableCollection<MenuItem> Items { get; set; }
    public string? UrlPathSegment => throw new System.NotImplementedException();
    public IScreen HostScreen { get; }
    public Category SubCategory { get; }
    public ICommand MenuItemClickedCommand { get; }
    private readonly IProductService _productService;
    public ObservableCollection<Product> Products { get; set; } = new ObservableCollection<Product>();
    public ReactiveCommand<Unit, Unit> LoadProductsCommand { get; }

    public MenuViewModel(IScreen screen, Category subCategory, IProductService productService)
    {
        this.HostScreen = screen;
        this.SubCategory = subCategory;
        this.MenuItemClickedCommand = ReactiveCommand.Create<Product>(HandleClickMenuItem);

        this._productService = productService;
        LoadProductsCommand = ReactiveCommand.CreateFromTask(LoadProductsAsync);
        LoadProductsCommand.Execute().Subscribe();

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

    private async Task LoadProductsAsync()
    {
        try
        {
            Console.WriteLine($"Loading products...{_productService}");
            Console.WriteLine($"SubCategory...{this.SubCategory.Name}");
            var products = await _productService.GetProductsByCategoryAsync(this.SubCategory);
            if (products.Any())
            {
                Products.Clear();
                foreach (var product in products)
                {
                    Products.Add(product);
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
    }

    public ReactiveCommand<Unit, IRoutableViewModel> GoBack => this.HostScreen.Router.NavigateBack;

    public void HandleClickMenuItem(Product menuItem)
    {
        this.HostScreen.Router.Navigate.Execute(new OptionsViewModel(this.HostScreen, menuItem, _productService));
    }
}