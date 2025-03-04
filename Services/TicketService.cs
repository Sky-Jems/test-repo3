using POS.Models;

namespace POS.Services;

/// <summary>
/// Represents a service for managing ticket.
/// </summary>
public class TicketService
{
    public TicketService()
    {

    }

    /// <summary>
    /// Create a ticket.
    /// </summary>
    /// <param name="products">List products to be order.</param>
    /// <returns>An instance of ticket.</returns>
    public Ticket CreateTicket(List<Product> products)
    {

    }

    /// <summary>
    /// Get all tickets.
    /// </summary>
    /// <param name="ticketId">Unique identifier of the ticket.</param>
    /// <returns>A list of tickets.</returns>
    public List<Ticket> GetAllTickets()
    {

    }

    /// <summary>
    /// Get all ticket by status filter.
    /// </summary>
    /// <param name="status">Status that will be use to filter tickets.</param>
    /// <returns>A list of tickets.</returns>
    public List<Ticket> GetAllTicketByStatus(TicketService status)
    {

    }

    /// <summary>
    /// Get an instance of ticket.
    /// </summary>
    /// <param name="ticketId">Unique identifier of the ticket.</param>
    /// <returns>An instance of ticket.</returns>
    public Ticket GetTicketById(Guid ticketId)
    {

    }

    /// <summary>
    /// Update ticket status.
    /// </summary>
    /// <param name="ticketId">Unique identifier of the ticket.</param>
    /// <param name="status">New status of the ticket.</param>
    /// <returns>An instance of the updated ticket.</returns>
    public Ticket UpdateTicketStatus(Guid ticketId, TicketStatus status)
    {

    }

    /// <summary>
    /// Update ticket product item list.
    /// </summary>
    /// <param name="ticketId">Unique identifier of the ticket.</param>
    /// <param name="products">Updated list of product to be updated.</param>
    /// <returns>An instance of ticket with updated list.</returns>
    public Ticket UpdateTicketProductList(Guid ticketId, List<Product> products)
    {

    }
}
