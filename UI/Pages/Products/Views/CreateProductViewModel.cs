using Avalonia;
using pos.Api;
using pos.Models.EventArgs;
using pos.Util;
using Pos.Models;
using ReactiveUI;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Reactive.Linq;
using System.Threading.Tasks;

namespace Pos.Pages.Products;

public partial class CreateProductViewModel : ReactiveObject, IRoutableViewModel
{
    public ObservableCollection<OptionGroup> ProductOptions { get; set; } = [];
    public ObservableCollection<Combinations> CombinationList { get; set; } = [];
    public ObservableCollection<ComboBoxVariants> CombinationFilterList { get; set; } = [];
    public ObservableCollection<Category> CategoryList { get; set; } = [];
    public ObservableCollection<Category> SelectedCategoryList { get; set; } = [];
    public ObservableCollection<string> AttributeList { get; set; }
    public ObservableCollection<OptionItem> SelectedVariantValues { get; set; } = new();
    public ReactiveCommand<Unit, Unit> NextCommand { get; }
    public ReactiveCommand<Unit, Unit> SaveVariantGroupsCommand { get; }
    public ReactiveCommand<OptionGroup, Unit> DeleteVariantGroupCommand { get; }
    public ReactiveCommand<Unit, Unit> AddVariantGroupCommand { get; }
    public ReactiveCommand<Category, Unit> RemoveCategoryFromListCommand { get; }
    public ReactiveCommand<OptionItem, Unit> RemoveOptionItemCommand { get; }
    public ReactiveCommand<Unit, Unit> LoadCategoryCommand { get; }
    public IScreen HostScreen { get; }
    public readonly IProductService _productService;
    public readonly ICategoryService _categoryService;

    public event EventHandler<NotificationEventArgs> TriggerNotif;
    public string VariantSummary { get; set; }
    public string? UrlPathSegment => throw new System.NotImplementedException();
    private string _productName = string.Empty;
    private string _descriptionName = string.Empty;
    private long? _editingProductId = null;
    private OptionGroup _selectedVariantGroup;
    public OptionGroup SelectedVariantGroup
    {
        get => _selectedVariantGroup;
        set
        {
            if (_selectedVariantGroup != value)
            {
                _selectedVariantGroup = value;
                if (_selectedVariantGroup != null)
                {
                    OnSelectedVariantGroupChanged();
                }
            }
        }
    }
    private bool _showClearIcon;
    public bool ShowClearIcon
    {
        get => _showClearIcon;
        set => this.RaiseAndSetIfChanged(ref _showClearIcon, value);
    }
    private bool _showVariantSummary;
    public bool ShowVariantSummary
    {
        get => _showVariantSummary;
        set => this.RaiseAndSetIfChanged(ref _showVariantSummary, value);
    }
    public string ProductName
    {
        get => _productName;
        set => this.RaiseAndSetIfChanged(ref _productName, value);
    }
    public string DescriptionName
    {
        get => _descriptionName;
        set => this.RaiseAndSetIfChanged(ref _descriptionName, value);
    }
    private long? _addedProductId;
    public long? AddedProductId
    {
        get => _addedProductId;
        private set => this.RaiseAndSetIfChanged(ref _addedProductId, value);
    }
    private OptionItem _selectedVariantValue;
    public OptionItem SelectedVariantValue
    {
        get => _selectedVariantValue;
        set
        {
            this.RaiseAndSetIfChanged(ref _selectedVariantValue, value);
            if (value != null)
            {
                LoadProductVariantsAsync();
            }
        }
    }
    public CreateProductViewModel(IScreen screen, IProductService productService, ICategoryService categoryService)
    {
        HostScreen = screen;
        _productService = productService;
        _categoryService = categoryService;
        ShowClearIcon = true;
        ShowVariantSummary = true;
        RemoveCategoryFromListCommand = ReactiveCommand.Create<Category>(RemoveCategoryFromList);
        DeleteVariantGroupCommand = ReactiveCommand.CreateFromTask<OptionGroup>(DeleteVariantGroupAsync);
        AddVariantGroupCommand = ReactiveCommand.Create(AddVariantGroup);
        SaveVariantGroupsCommand = ReactiveCommand.CreateFromTask(SaveVariantGroup);
        LoadCategoryCommand = ReactiveCommand.CreateFromTask(LoadCategoryAsync);
        RemoveOptionItemCommand = ReactiveCommand.Create<OptionItem>(RemoveOptionValue);
    }

    private void RemoveOptionValue(OptionItem item)
    {
        if (item == null || _editingProductId != null) return;

        foreach (var group in ProductOptions)
        {
            if (group.Values.Contains(item))
            {
                group.Values.Remove(item);
                break;
            }
        }
    }
    private async Task LoadCategoryAsync()
    {
        var categories = await _categoryService.GetAllCategoriesAsync();
        CategoryList.Clear();
        foreach (var category in categories)
        {
            CategoryList.Add(category);
        }
    }

    private async Task SaveVariantGroup()
    {
        try
        {
            if (AddedProductId is null && _editingProductId is null)
            {
                TriggerNotif?.Invoke(this, new NotificationEventArgs
                {
                    Message = "Add a product first before adding variant groups.",
                    NotifType = NotifConstants.NotifType.Error
                });
                return;
            }
            var productId = AddedProductId ?? _editingProductId;
            var convertedVariants = new List<OptionGroupDto> { };
            foreach (var group in ProductOptions)
            {
                if (!string.IsNullOrWhiteSpace(group.Name))
                {
                    List<OptionItem> convertedProductOptions = [];
                    foreach (var option in group.Values)
                    {
                        var optionItem = new OptionItem
                        {
                            Id = option.Id,
                            Value = option.Value
                        };
                        convertedProductOptions.Add(optionItem);
                    }

                    if (group.Id != null)
                    {
                        var updatedGroup = new OptionGroupDto
                        {
                            Id = group.Id,
                            Name = group.Name,
                            ProductId = group.ProductId,
                            Values = convertedProductOptions
                        };
                        await _productService.UpdateVariantAsync(updatedGroup);
                        continue;
                    }

                    convertedVariants.Add(new OptionGroupDto
                    {
                        Id = group.Id,
                        Name = group.Name,
                        ProductId = (long)productId,
                        Values = convertedProductOptions
                    });
                }
            }

            if (convertedVariants.Any() || AddedProductId != null)
            {
                await _productService.AddVariantsAsync(convertedVariants, AddedProductId);
                await Task.Delay(1000);
                ProductOptions.Clear();
                foreach (var option in await _productService.GetProductOptionsAsync(productId.Value))
                {
                    ProductOptions.Add(new OptionGroup
                    {
                        tableId = ProductOptions.Count(),
                        Id = option.Id,
                        Name = option.Name,
                        ProductId = option.ProductId,
                        Values = new ObservableCollection<OptionItem>(
                            await _productService.GetProductOptionValuesAsync((int)option.Id))
                    });
                }
                TriggerNotif?.Invoke(this, NotificationUtil.Success("Variant groups added successfully."));
            }
            else
            {
                TriggerNotif?.Invoke(this, NotificationUtil.Success("Variant groups updated successfully."));
            }
            if (productId.HasValue)
            {
                await Task.Delay(1000);
                await LoadVariantCombinationList((int)productId);
            }
        }
        catch (Exception e)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Error($"error occured : {e}"));
        }
    }

    private void AddVariantGroup()
    {
        var newGroup = new OptionGroup { tableId = ProductOptions.Count() };
        ProductOptions.Add(newGroup);
    }

    private async Task DeleteVariantGroupAsync(OptionGroup group)
    {
        if (group == null || !ProductOptions.Contains(group)) return;

        if (group.Id != null)
        {
            try
            {
                await _productService.DeleteVariantByIdAsync((int)group.Id.Value);
            }
            catch (Exception ex)
            {
                TriggerNotif?.Invoke(this, NotificationUtil.Error("Failed to delete variant group from server."));
                return;
            }
        }

        ProductOptions.Remove(group);
    }

    public async Task SetProductForEditAsync(long productId)
    {
        var product = await _productService.GetProductByIdAsync((int)productId);

        _editingProductId = product.Id;
        ShowClearIcon = _editingProductId == null;
        ProductName = product.Name;
        DescriptionName = product.Description;
        SelectedCategoryList.Clear();

        foreach (Category category in product.Categories)
        {
            SelectedCategoryList.Add(category);
        }
    }

    public async Task AddOrEditProductAsync()
    {
        try
        {
            if (string.IsNullOrWhiteSpace(ProductName))
            {
                TriggerNotif?.Invoke(this, NotificationUtil.Error("Product name is required."));
                return;
            }
            var product = new ProductDto
            {
                Id = _editingProductId,
                Name = ProductName,
                Description = DescriptionName,
                Categories = SelectedCategoryList.Select(p => p.Id).ToList()
            };

            if (_editingProductId != null)
            {
                await _productService.UpdateProductAsync(product);
                ProductOptions.Clear();
                foreach (var option in await _productService.GetProductOptionsAsync(_editingProductId.Value))
                {

                    ProductOptions.Add(new OptionGroup
                    {
                        tableId = ProductOptions.Count(),
                        Id = option.Id,
                        Name = option.Name,
                        ProductId = option.ProductId,
                        Values = new ObservableCollection<OptionItem>(await _productService.GetProductOptionValuesAsync((int)option.Id))
                    });
                }

                TriggerNotif?.Invoke(this, new NotificationEventArgs
                {
                    Message = "Product has been updated.",
                    NotifType = NotifConstants.NotifType.Success
                });
            }
            else
            {
                AddedProductId = await _productService.AddProduct(product);
                TriggerNotif?.Invoke(this, NotificationUtil.Success("Product has been added."));
            }
        }
        catch (Exception)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Error("Product created failed."));
        }
    }

    public void RemoveCategoryFromList(Category category)
    {
        SelectedCategoryList.Remove(category);
    }
    public void OnSelectedVariantGroupChanged()
    {
        SelectedVariantValues.Clear();

        if (SelectedVariantGroup?.Values != null)
        {
            foreach (var item in SelectedVariantGroup.Values)
            {
                SelectedVariantValues.Add(item);
            }
        }
    }

    public async Task UpdateVariantCombination(Combinations item)
    {
        var variantPayload = new
        {
            id = item.VariantId,
            product_id = item.Product.Id,
            sku = item.SKU,
            price = item.Price
        };
        await _productService.UpdateVariantCombinationAsync(item.VariantId, variantPayload);
    }

    private async Task LoadVariantCombinationList(int id)
    {
        CombinationList.Clear();

        var items = await _productService.GetVariantCombinationListAsync(id);
        if (items?.Any() == true)
        {
            foreach (var item in items)
            {
                item.VariantSummary = item.VariantCombinations != null && item.VariantCombinations.Any()
                                      ? string.Join(" / ", item.VariantCombinations.Select(v => v.Value))
                                      : "";
                ShowVariantSummary = !string.IsNullOrEmpty(item.VariantSummary);
                CombinationList.Add(item);
            }
        }
        else
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Warning("No combinations found"));
        }
    }


    private async void LoadProductVariantsAsync()
    {
        try
        {
            long? productId = _editingProductId ?? _addedProductId;
            int variantOptionId = (int)SelectedVariantValue.Id;

            var combinations = await _productService.GetProductVariantsAsync((long)productId, variantOptionId);

            CombinationList.Clear();
            foreach (var combination in combinations)
            {
                combination.VariantSummary = combination.VariantCombinations != null && combination.VariantCombinations.Any()
                                      ? string.Join(" / ", combination.VariantCombinations.Select(v => v.Value))
                                      : "";
                ShowVariantSummary = !string.IsNullOrEmpty(combination.VariantSummary);
                CombinationList.Add(combination);
            }
        }
        catch (Exception ex)
        {
            TriggerNotif?.Invoke(this, NotificationUtil.Error("Load product variant error occured"));
        }
    }

    public ReactiveCommand<Unit, IRoutableViewModel> GoBack => HostScreen.Router.NavigateBack;
}