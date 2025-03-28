using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.Domain.Entities;

public class OrderItem
{
    public int Id { get; set; }
    public required Product Product { get; set; }
    public int Quantity { get; set; }

    public List<OrderItemOption> OrderItemOption { get; set; } = [];
    public OrderItemStatus Status { get; set; } = OrderItemStatus.PENDING;
    public Order? Order { get; set; }
    public Ticket? Ticket { get; set; }
}

public class OrderItemOption
{
    public int Id { get; set; }
    public int OrderItemId { get; set; }
    public int OptionGroupId { get; set; }
    public int OptionItemId { get; set; }
}