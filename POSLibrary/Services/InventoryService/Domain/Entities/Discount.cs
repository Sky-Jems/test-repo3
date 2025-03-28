namespace POSLibrary.Services.InventoryService.Domain.Entities;

public class Discount
{
    public int Id { get; set; }
    public required string Name { get; set; }
    public required int Deal { get; set; }
    public DateTime ValidUntil { get; set; }
    public DateTime CreatedAt { get; set; }
}