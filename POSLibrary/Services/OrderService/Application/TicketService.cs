using POSLibrary.Services.OrderService.Application.Interface;
using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.Application;

public class TicketService(ITicketRepository ticketRepository) : ITicketService
{
    private readonly ITicketRepository _ticketRepository = ticketRepository;

    public Ticket CreateTicket(int orderId, List<OrderItem> orderItems)
    {
        return this._ticketRepository.CreateTicket(orderItems);
    }

    public List<Ticket> GetTickets(OrderItemStatus? status = null)
    {
        return this._ticketRepository.GetTickets(status);
    }

    public Ticket GetTicket(int ticketId)
    {
        return this._ticketRepository.GetTicket(ticketId);
    }

    public void UpdateTicket(int id, List<OrderItem> orderItems)
    {
        var ticket = this._ticketRepository.GetTicket(id);
        if (ticket == null)
        {
            throw new ArgumentException($"Ticket id: {id} does not exist");
        }
        this._ticketRepository.UpdateItems(id, orderItems);
    }

    public void UpdateTicketItem(int itemId, OrderItem orderItem)
    {
        this._ticketRepository.UpdateTicketItem(itemId, orderItem);
    }

    public void RemoveTicketItem(int ticketId, int ticketItemId)
    {
        this._ticketRepository.RemoveTicketItem(ticketId, ticketItemId);
    }
}