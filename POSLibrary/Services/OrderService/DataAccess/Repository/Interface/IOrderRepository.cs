using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.DataAccess.Repository.Interface;

public interface IOrderRepository
{
    public int CreateOrder(Order orders);
    public List<Order> GetOrdersByTableNumber(string tableNumber);
    public Order? GetOrderById(int orderId);
    public List<Order> GetAll(OrderStatus status = OrderStatus.PENDING);
    public int UpdateOrder(Order order);
}