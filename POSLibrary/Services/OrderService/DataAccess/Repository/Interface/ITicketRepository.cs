using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.DataAccess.Repository.Interface;

public interface ITicketRepository
{
    public Ticket CreateTicket(List<OrderItem> orderItems);
    public List<Ticket> GetTickets(OrderItemStatus? status);
    public Ticket GetTicket(int ticketId);
    public void UpdateItems(int ticketId, List<OrderItem> orderItems);
    public int UpdateTicketItem(int ticketItemId, OrderItem orderItem);
    public void RemoveTicketItem(int ticketId, int ticketItemId);
}