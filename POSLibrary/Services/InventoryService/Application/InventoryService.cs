using POSLibrary.Services.InventoryService.Application.Interface;
using POSLibrary.Services.InventoryService.DataAccess.Repository.Interface;
using POSLibrary.Services.InventoryService.Domain.Entities;

namespace POSLibrary.Services.InventoryService.Application;

public class InventoryService(IInventoryRepository inventoryRepository) : IInventoryService
{
    private readonly IInventoryRepository _inventoryRepository = inventoryRepository;

    public Product AddProduct(string sku, string name, float price, Category category)
    {
        return this._inventoryRepository.AddProduct(sku, name, price, category);
    }

    public List<Product> GetProducts()
    {
        return this._inventoryRepository.GetProducts();
    }

    public Product? GetProductById(int id)
    {
        var product = this._inventoryRepository.GetProductById(id);
        if (product == null)
        {
            return null;
        }
        return product;
    }

    public List<Product> GetProductsByCategory(string category)
    {
        return this._inventoryRepository.GetProductsByCategory(category);
    }

    public Category AddCategory(string name, string type)
    {
        return this._inventoryRepository.AddCategory(name, type);
    }

    public List<Category> GetCategories()
    {
        return this._inventoryRepository.GetCategories();
    }

    public ProductOption? GetProductOption(int productId, int OptionGroupId, int OptionItemId)
    {
        return this._inventoryRepository.GetProductOption(productId, OptionGroupId, OptionItemId);
    }
}