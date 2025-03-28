using POSLibrary.Services.InventoryService.Domain.Entities;

namespace POSLibrary.Services.InventoryService.Application.Interface;

public interface IInventoryService
{
    public Product AddProduct(string sku, string name, float price, Category category);
    public List<Product> GetProducts();
    public Product? GetProductById(int id);
    public List<Product> GetProductsByCategory(string category);
    public Category AddCategory(string name, string type);
    public List<Category> GetCategories();
}