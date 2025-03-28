using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.DataAccess.Repository;

public class TicketRepository : ITicketRepository
{
    public Ticket CreateTicket(List<OrderItem> orderItems)
    {
        throw new NotImplementedException();
    }

    public List<Ticket> GetTickets(OrderItemStatus? status)
    {
        throw new NotImplementedException();
    }

    public Ticket GetTicket(int ticketId)
    {
        throw new NotImplementedException();
    }
    
    public void UpdateItems(int ticketId, List<OrderItem> orderItems)
    {
        throw new NotImplementedException();
    }

    public int UpdateTicketItem(int ticketItemId, OrderItem orderItem)
    {
        return ticketItemId;
    }

    public void RemoveTicketItem(int ticketId, int ticketItemId)
    {
        throw new NotImplementedException();
    }
}