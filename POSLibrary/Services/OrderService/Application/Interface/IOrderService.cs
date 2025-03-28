using POSLibrary.Services.OrderService.Domain.Entities;

namespace POSLibrary.Services.OrderService.Application.Interface;

public interface IOrderService
{
    public int CreateOrder(Order orders);
    public List<Order> GetOrdersByTableNumber(string tableNumber);
    public Order? GetOrderById(int orderId);
    public List<Order> GetPendingOrders();
    public int UpdateOrderItem(Order order, OrderItem updatedOrderItem);
    public int AddOrderItems(Order order, List<OrderItem> additionalOrderItem);
    public int UpdateOrder(Order order);
    public int RemoveOrderItem(Order order, OrderItem orderItem);
}