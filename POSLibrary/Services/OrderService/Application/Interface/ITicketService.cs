using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.Application.Interface;

public interface ITicketService
{
    /// <summary>
    /// Create a ticket.
    /// </summary>
    /// <param name="items">List of order items.</param>
    /// <returns>An instance of ticket.</returns>
    public Ticket CreateTicket(int orderId, List<OrderItem> orderItems);

    /// <summary>
    /// Get all tickets by default, filters by status when value is passed.
    /// </summary>
    /// <param name="status">Filter by status.</param>
    /// <returns>A list of tickets.</returns>
    public List<Ticket> GetTickets(OrderItemStatus? status = null);

    /// <summary>
    /// Get an instance of ticket.
    /// </summary>
    /// <param name="ticketId">Unique identifier of the ticket.</param>
    /// <returns>An instance of ticket.</returns>
    public Ticket GetTicket(int ticketId);

    /// <summary>
    /// Update ticket order item list.
    /// </summary>
    /// <param name="ticketId">Unique identifier of the ticket.</param>
    /// <param name="orderItems">Updated list of product items to be updated.</param>
    public void UpdateTicket(int id, List<OrderItem> orderItems);
    public void UpdateTicketItem(int itemId, OrderItem orderItem);

    public void RemoveTicketItem(int ticketId, int ticketItemId);
}