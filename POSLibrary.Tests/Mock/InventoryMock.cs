using Moq;
using POSLibrary.Services.InventoryService.Application;
using POSLibrary.Services.InventoryService.DataAccess.Repository.Interface;
using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Domain.Entities;

namespace POSLibrary.Tests.Mock;

public class MockInventoryService
{
    public IInventoryRepository inventoryRepository;
    public List<Category> CategoriesData = [
        new Category
        {
            Name = "Pasta",
            Type = "Food"
        },
        new Category
        {
            Name = "Pepzi",
            Type = "Drink"
        },
    ];
    public List<Product> ProductsData = [
        new Product
        {
            Id = 1,
            Sku = "Sku-123",
            Name = "Pasta",
            Price = 100,
            Category = new Category
            {
                Name = "Pasta",
                Type = "Food"
            },
        },
        new Product
        {
            Id = 2,
            Sku = "Sku-123",
            Name = "Pasta",
            Price = 100,
            Category = new Category
            {
                Name = "Pasta",
                Type = "Food"
            },
        },
        new Product
        {
            Id = 3,
            Sku = "Sku-123",
            Name = "Pasta",
            Price = 100,
            Category = new Category
            {
                Name = "Pasta",
                Type = "Food"
            },
        },
    ];

    public MockInventoryService()
    {
        var mockInventory = new Mock<IInventoryRepository>();

        mockInventory.Setup(inventoryRepository => inventoryRepository.AddProduct(
            this.ProductsData[0].Sku,
            this.ProductsData[0].Name,
            this.ProductsData[0].Price,
            this.ProductsData[0].Category))
            .Returns(this.ProductsData[0]);
        mockInventory.Setup(inventoryRepository => inventoryRepository.AddCategory(this.CategoriesData[0].Name, this.CategoriesData[0].Type)).Returns(this.CategoriesData[0]);
        mockInventory.Setup(inventoryRepository => inventoryRepository.GetCategories()).Returns(this.CategoriesData);
        mockInventory.Setup(inventoryRepository => inventoryRepository.GetProducts()).Returns(this.ProductsData);
        mockInventory.Setup(inventoryRepository => inventoryRepository.GetProductById(this.ProductsData[0].Id)).Returns(this.ProductsData[0]);
        mockInventory.Setup(inventoryRepository => inventoryRepository.GetProductsByCategory(this.ProductsData[0].Category.Name)).Returns(this.ProductsData);

        this.inventoryRepository = mockInventory.Object;
    }


    public InventoryService Setup()
    {
        return new(inventoryRepository);
    }
}