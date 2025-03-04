using POS.Models;

namespace POS.Services;

public class InventoryService
{
    private List<Product> _products;
    private List<Category> _categories;
    public InventoryService()
    {
        // initializes _products and _categories to empty list
    }

    public Product GetProductById(Guid productId)
    {
        // Get product by ID
    }
    public List<Product> GetProductsByCategory(string category)
    {
        // Get all products by category
    }
    public List<Category> GetCategories()
    {
        // Returns all valid and unique categories
    }
}