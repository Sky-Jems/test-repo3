
namespace POSLibrary.Services.OrderService.Domain.Entities;

public class Ticket
{
    public int Id { get; set; }
    public int TicketNumber { get; set; }
    public required List<OrderItem> Items { get; set; }
    public Order? Order { get; set; }
}