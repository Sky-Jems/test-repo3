using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.DataAccess.Repository;

public class OrderRepository : IOrderRepository
{
    private readonly List<Order> _orders = [];
    private readonly Random random = new();

    public int CreateOrder(Order order)
    {
        order.Id = random.Next();
        order.OrderStatus = OrderStatus.PENDING;
        this._orders.Add(order);
        return order.Id;
    }

    public List<Order> GetOrdersByTableNumber(string tableNumber)
    {
        return this._orders.FindAll(order => order.TableNumber == tableNumber);
    }

    public Order? GetOrderById(int orderId)
    {
        return this._orders.Find(order => order.Id == orderId);
    }

    public List<Order> GetAll(OrderStatus status = OrderStatus.PENDING)
    {
        var orders = this._orders.FindAll(order => order.OrderStatus == status);
        return orders;
    }

    public int UpdateOrder(Order order)
    {
        return order.Id;
    }
}