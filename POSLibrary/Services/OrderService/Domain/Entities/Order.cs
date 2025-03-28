using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.Domain.Entities;

public class Order
{
    public int Id;
    public required string CustomerName;
    public required string TableNumber;
    public required List<OrderItem> Items;
    public float Subtotal;
    public float Discounts;
    public OrderStatus OrderStatus;
    public List<Ticket>? Tickets { get; set; }

    public void UpdateStatus()
    {
        // Updates Order status
    }

    public float Total()
    {
        // Calculate SubTotal price minus the discounts.
        return 0;
    }

    public void GenerateBill()
    {
        // Generates Bill to be passed to the Payment service
    }
}