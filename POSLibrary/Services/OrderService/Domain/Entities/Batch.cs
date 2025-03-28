namespace POSLibrary.Services.OrderService.Domain.Entities;

public class Batch
{
    public int Id { get; set; }
    public required List<OrderItem> Items { get; set; }
}