namespace POS.Models;

enum TicketStatus
{
    PENDING,
    PREPARING,
    READY,
    CANCELLED,
    COMPLETE
}
public class Ticket
{
    public int id { get; set; };
    public List<Product> product { get; set; };
    public TicketStatus _ticketStatus { get; set; } = TicketStatus.PENDING;
}