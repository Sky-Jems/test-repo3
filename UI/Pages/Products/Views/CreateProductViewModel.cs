using pos.Api;
using Pos.Util;
using Pos.Models;
using ReactiveUI;
using ReactiveUI.Fody.Helpers;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Threading.Tasks;
using pos.Models.EventArgs;

namespace Pos.Pages.Products;

public partial class CreateProductViewModel : ReactiveObject, IRoutableViewModel
{
    #region Observables
    public ObservableCollection<OptionGroup> ProductOptions { get; set; } = [];
    public ObservableCollection<Category> CategoryList { get; set; } = [];
    public ObservableCollection<Category> SelectedCategoryList { get; set; } = [];
    public ObservableCollection<string> AttributeList { get; set; }

    [Reactive] public string ProductName { get; set; } = string.Empty;
    [Reactive] public string Price { get; set; } = string.Empty;
    [Reactive] public string DescriptionName { get; set; } = string.Empty;
    [Reactive] public long? AddedProductId { get; set; }
    [Reactive] public long? EditingProductId { get; set; }

    #endregion

    #region ReactiveCommands
    public ReactiveCommand<Unit, Unit> NextCommand { get; }
    public ReactiveCommand<Category, Unit> RemoveCategoryFromListCommand { get; }
    public ReactiveCommand<Unit, Unit> LoadCategoryCommand { get; }
    #endregion

    #region Services
    public readonly IProductService _productService;
    public readonly ICategoryService _categoryService;
    #endregion

    #region Other Variables
    public IScreen HostScreen { get; }
    public event EventHandler<NotificationEventArgs> TriggerNotif;
    public string? UrlPathSegment => throw new NotImplementedException();
    #endregion


    public CreateProductViewModel(IScreen screen, IProductService productService, ICategoryService categoryService)
    {
        HostScreen = screen;
        _productService = productService;
        _categoryService = categoryService;
        RemoveCategoryFromListCommand = ReactiveCommand.Create<Category>(RemoveCategoryFromList);
        LoadCategoryCommand = ReactiveCommand.CreateFromTask(LoadCategoryAsync);
    }

    private async Task LoadCategoryAsync()
    {
        List<Category> categories = await _categoryService.GetAllCategoriesAsync();
        CategoryList.Clear();
        foreach (var category in categories)
        {
            CategoryList.Add(category);
        }
    }

    public async Task SetProductForEditAsync(long productId)
    {
        var product = await _productService.GetProductByIdAsync((int)productId);

        EditingProductId = product.Id;
        ProductName = product.Name;
        DescriptionName = product.Description;
        Price = product.Price.ToString();
        SelectedCategoryList.Clear();

        var categoryList = new List<Category>();

        if (product.CategoryIds != null)
        {
            foreach (var id in product.CategoryIds)
            {
                if (id != null)
                {
                    var category = await _categoryService.GetCategoryByIdAsync((int)id);
                    if (category != null)
                    {
                        categoryList.Add(category);
                        SelectedCategoryList.Add(category);
                    }
                }
            }
        }

        product.Categories = categoryList;
    }

    public async Task<bool> AddOrEditProductAsync()
    {
        ProductName = ProductName.Trim();
        DescriptionName = DescriptionName.Trim();
        Price = Price.Trim();

        if (string.IsNullOrWhiteSpace(ProductName))
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Warning("Product name is required."));
            return false;
        }

        if (SelectedCategoryList == null || !SelectedCategoryList.Any())
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Warning("At least one category must be selected."));
            return false;
        }

        if (!decimal.TryParse(Price, out var parsedPrice) || parsedPrice <= 0)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Warning("A valid price greater than 0 is required."));
            return false;
        }

        try
        {
            var product = new Product
            {
                Id = EditingProductId,
                Name = ProductName,
                Description = DescriptionName,
                CategoryIds = SelectedCategoryList.Select(p => p.Id.GetValueOrDefault()).ToList(),
                Price = parsedPrice
            };

            if (EditingProductId != null)
            {
                await _productService.UpdateProductAsync(product);
                TriggerNotif?.Invoke(this, NotificationUtil.Success("Product has been updated."));
            }
            else
            {
                AddedProductId = await _productService.AddProduct(product);
                TriggerNotif?.Invoke(this, NotificationUtil.Success("Product has been added."));
            }
            return true;
        }
        catch (Exception)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Error("Product creation failed."));
            return false;
        }
    }

    public void RemoveCategoryFromList(Category category)
    {
        SelectedCategoryList.Remove(category);
    }

    public void SetCategoryList(IEnumerable<Category> categories)
    {
        foreach (var category in categories)
        {
            if (SelectedCategoryList.All(c => c.Id != category.Id))
            {
                SelectedCategoryList.Add(category);
            }
        }
    }

    public ReactiveCommand<Unit, IRoutableViewModel> GoBack => HostScreen.Router.NavigateBack;
}