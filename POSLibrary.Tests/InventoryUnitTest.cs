using POSLibrary.Services.InventoryService.Application;
using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Tests.Mock;

namespace POSLibrary.Tests;

public class InventoryUnitTest
{
    protected InventoryService InventoryService { get; private set; }
    private MockInventoryService moqInventory; 

    [SetUp]
    public void Setup()
    {
        this.moqInventory = new MockInventoryService();
        this.InventoryService = this.moqInventory.Setup();
    }

    [Test]
    public void CreateProductTest()
    {
        Product arg = this.moqInventory.ProductsData[0];
        var product = this.InventoryService.AddProduct(arg.Sku, arg.Name, arg.Price, arg.Category);

        Assert.Multiple(() =>
        {
            Assert.That(actual: arg.Sku, Is.EqualTo(product.Sku));
            Assert.That(actual: arg.Name, Is.EqualTo(product.Name));
            Assert.That(actual: arg.Price, Is.EqualTo(product.Price));
            Assert.That(actual: arg.Category.Name, Is.EqualTo(product.Category.Name));
            Assert.That(actual: arg.Category.Type, Is.EqualTo(product.Category.Type));
        });
    }

    [Test]
    public void CreateCategoryTest()
    {
        Category arg = this.moqInventory.CategoriesData[0];
        var category = this.InventoryService.AddCategory(arg.Name, arg.Type);

        Assert.Multiple(() =>
        {
            Assert.That(actual: arg.Name, Is.EqualTo(category.Name));
            Assert.That(actual: arg.Type, Is.EqualTo(category.Type));
        });
    }

    [Test]
    public void GetCategoriesTest()
    {
        var categories = this.InventoryService.GetCategories();
        Assert.That(actual: categories, Has.Count.EqualTo(this.moqInventory.CategoriesData.Count));
    }

    [Test]
    public void GetAllProductsTest()
    {
        var inventoryProducts = this.InventoryService.GetProducts();
        Assert.That(actual: inventoryProducts, Has.Count.EqualTo(this.moqInventory.ProductsData.Count));
    }

    [Test]
    public void GetProductByIdTest()
    {
        Product arg = this.moqInventory.ProductsData[0];
        var product = this.InventoryService.GetProductById(this.moqInventory.ProductsData[0].Id);

        Assert.Multiple(() =>
        {
            Assert.That(product, Is.Not.Null);
            Assert.That(actual: arg.Id, Is.EqualTo(product?.Id));
            Assert.That(actual: arg.Sku, Is.EqualTo(product?.Sku));
            Assert.That(actual: arg.Name, Is.EqualTo(product?.Name));
            Assert.That(actual: arg.Price, Is.EqualTo(product?.Price));
            Assert.That(actual: arg.Category.Id, Is.EqualTo(product?.Category.Id));
            Assert.That(actual: arg.Category.Name, Is.EqualTo(product?.Category.Name));
            Assert.That(actual: arg.Category.Type, Is.EqualTo(product?.Category.Type));
        });
    }

    [Test]
    public void GetProductsByCategoryTest()
    {
        Product arg = this.moqInventory.ProductsData[0];
        var products = this.InventoryService.GetProductsByCategory(arg.Category.Name);

        Assert.Multiple(() =>
        {
            Assert.That(products, Has.Count.EqualTo(3));
            Assert.That(actual: arg.Id, Is.EqualTo(products[0].Id));
            Assert.That(actual: arg.Sku, Is.EqualTo(products[0].Sku));
            Assert.That(actual: arg.Name, Is.EqualTo(products[0].Name));
            Assert.That(actual: arg.Price, Is.EqualTo(products[0].Price));
            Assert.That(actual: arg.Category.Id, Is.EqualTo(products[0].Category.Id));
            Assert.That(actual: arg.Category.Name, Is.EqualTo(products[0].Category.Name));
            Assert.That(actual: arg.Category.Type, Is.EqualTo(products[0].Category.Type));
        });
    }
}