namespace POSLibrary.Services.InventoryService.Domain.Entities;

public class Product()
{
    public int Id { get; set; }
    public required string Sku { get; set; }
    public required string Name { get; set; }
    public required float Price { get; set; }
    public string? Description { get; set; }
    public required Category Category { get; set; }
    public ICollection<ProductOption> ProductOption { get; set; } = [];
}

public class OptionGroup()
{
    public int Id { get; set; }
    public required string Name { get; set; }
}

public class OptionItem()
{
    public int Id { get; }
    public required string Name { get; set; }
    public required float Price { get; set; }
}

public class ProductOption()
{
    public int ProductId { get; set; }
    public int OptionGroupId { get; set; }
    public int OptionItemId { get; set; }
}