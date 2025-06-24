using System.Collections.ObjectModel;
using ReactiveUI;
using pos.Api;
using pos.Util;
using Pos.Models;
using System.Reactive;
using System.Threading.Tasks;
using System.Linq;
using System;
using pos.Models.EventArgs;
using System.Reactive.Linq;
using System.Collections.Generic;
using pos.Extensions;
using Microsoft.Extensions.DependencyInjection;

namespace Pos.Pages.Products;

public partial class MainViewModel : ReactiveObject, IRoutableViewModel
{
    public event EventHandler<NotificationEventArgs> TriggerNotif;
    public string? UrlPathSegment => throw new System.NotImplementedException();

    public IProductService _productService;
    public readonly ICategoryService _categoryService;
    public IScreen HostScreen { get; }

    public ObservableCollection<Product> Products { get; } = new();
    private List<Product> _allProducts = new();
    public ObservableCollection<Category> ComboBoxItems { get; } = new();
    public string ProductCountText => $"Products ( {Products.Count} )";
    private Category? _selectedComboBoxItem = new Category { };
    public Category? SelectedComboBoxItem
    {
        get => _selectedComboBoxItem;
        set
        {
            this.RaiseAndSetIfChanged(ref _selectedComboBoxItem, value);
            FilterProducts();
        }
    }
    private string _searchText = "";
    public string SearchText
    {
        get => _searchText;
        set
        {
            this.RaiseAndSetIfChanged(ref _searchText, value);
            FilterProducts();
        }
    }
    public ReactiveCommand<Unit, Unit> LoadProductCommand { get; }
    public ReactiveCommand<Unit, Unit> LoadCategoriesCommand { get; }
    public ReactiveCommand<Unit, Unit> CreateNewProductCommand { get; }
    public ReactiveCommand<int, Unit> DeleteProductByIdCommand { get; }
    public ReactiveCommand<Product, Unit> EditProductCommand { get; }
    public MainViewModel(IScreen screen)
    {
        HostScreen = screen;
        _productService = ServiceLocator.Services.GetRequiredService<IProductService>();
        _categoryService = ServiceLocator.Services.GetRequiredService<ICategoryService>();
        Products = new ObservableCollection<Product>();
        Products.CollectionChanged += ProductsCollectionChanged;
        LoadProductCommand = ReactiveCommand.CreateFromTask(LoadAllProductsAsync);
        LoadCategoriesCommand = ReactiveCommand.CreateFromTask(LoadCategories);
        EditProductCommand = ReactiveCommand.CreateFromTask<Product>(EditProduct);
        LoadProductCommand.Execute();
        LoadCategoriesCommand.Execute();
    }

    private void ProductsCollectionChanged(object? sender, EventArgs args)
    {
        this.RaisePropertyChanged(nameof(ProductCountText));
    }

    private async Task EditProduct(Product product)
    {
        var createVm = new CreateProductViewModel(HostScreen, _productService, _categoryService);
        HostScreen.Router.Navigate.Execute(createVm);
    }

    public void LoadProductList()
    {
        LoadProductCommand.Execute().Subscribe();
    }

    private async Task LoadAllProductsAsync()
    {
        try
        {
            var products = await _productService.GetAllProducts();
            if (products.Any())
            {
                _allProducts = products.ToList();
                FilterProducts();
            }
        }
        catch (Exception)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Error("Product load failed."));
        }
    }

    public async Task DeleteProductById(int productId)
    {
        try
        {
            await _productService.DeleteProductByIdAsync(productId);
            LoadProductList();
            TriggerNotif?.Invoke(this, NotificationUtil.Success("Product has been deleted."));
        }
        catch (Exception)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Error("Product deletion failed."));
        }
    }

    private async Task LoadCategories()
    {
        try
        {
            var categories = await _categoryService.GetAllCategoriesAsync();
            ComboBoxItems.Clear();
            ComboBoxItems.Add(new Category { Id = 0, Name = "All Items" });

            foreach (var category in categories)
            {
                if (!string.IsNullOrWhiteSpace(category.Name))
                    ComboBoxItems.Add(category);
            }

            SelectedComboBoxItem = ComboBoxItems[0];
        }
        catch (Exception e)
        {
            Console.WriteLine(e.Message);
            TriggerNotif?.Invoke(this, new NotificationEventArgs
            {
                Message = "Failed to load categories",
                NotifType = NotifConstants.NotifType.Error
            });
        }
    }
    public void FilterProducts()
    {
        Products.Clear();
        IEnumerable<Product> filtered;
        if (string.IsNullOrWhiteSpace(SearchText))
        {
            filtered = _allProducts;
        }
        else
        {
            filtered = _allProducts.Where(p => p.Name?.Contains(SearchText, StringComparison.OrdinalIgnoreCase) == true);
        }

        if (SelectedComboBoxItem?.Name != null && SelectedComboBoxItem.Name != "All Items")
        {
            filtered = filtered.Where(p => p.CategoryNames.Contains(SelectedComboBoxItem.Name));
        }

        foreach (var product in filtered)
            Products.Add(product);
    }

    public void NavigateToCreateProduct()
    {
        HostScreen.Router.Navigate.Execute(new CreateProductViewModel(HostScreen, _productService, _categoryService));
    }
}