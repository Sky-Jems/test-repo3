using POS.Models;

namespace POS.Services;

public class OrderService
{
    private List<Order> _orders;

    public OrderService()
    {
        // Initializes _orders to empty list
    }
    public List<Order> GetPendingOrders()
    {
        // Gets all pending orders
    }
}