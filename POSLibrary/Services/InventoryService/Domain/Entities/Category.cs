namespace POSLibrary.Services.InventoryService.Domain.Entities;

public class Category
{
    public int Id { get; set; }
    public required string Name { get; set; }
    public required string Type { get; set; }
}