using POSLibrary.Services.InventoryService.DataAccess.Repository.Interface;
using POSLibrary.Services.InventoryService.Domain.Entities;

namespace POSLibrary.Services.InventoryService.DataAccess.Repository;

public class InventoryRepository() : IInventoryRepository
{
    private readonly List<Product> _products = [];
    private readonly List<Category> _categories = [];
    private readonly Random random = new();

    public Category AddCategory(string name, string type)
    {
        Category category = new()
        {
            Id = random.Next(),
            Name = name,
            Type = type
        };
        this._categories.Add(category);
        return category;
    }

    public Product AddProduct(string sku, string name, float price, Category category)
    {
        Product product = new()
        {
            Id = random.Next(),
            Sku = sku,
            Name = name,
            Price = price,
            Category = category,
        };
        this._products.Add(product);
        return product;
    }

    public List<Category> GetCategories()
    {
        return this._categories;
    }

    public Product? GetProductById(int id)
    {
        return this._products.Find(product => product.Id == id);
    }

    public List<Product> GetProducts()
    {
        return this._products;
    }

    public List<Product> GetProductsByCategory(string category)
    {
        return this._products.FindAll(
            product => product.Category.Name == category);
    }

    public ProductOption? GetProductOption(int productId, int OptionGroupId, int OptionItemId)
    {
        return new ProductOption
        {
            ProductId = 1,
            OptionGroupId = 1,
            OptionItemId = 1
        };

    }
}