using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.DataAccess.Repository;

public class OrderItemRepository : IOrderItemRepository
{
    public int UpdateOrderItem(OrderItem existingOrderItem, OrderItem updatedOrderItem)
    {
        existingOrderItem = updatedOrderItem;
        return existingOrderItem.Id;
    }
    public int AddOrderItems(Order order, List<OrderItem> additionalOrderItem)
    {
        order.Items.AddRange(additionalOrderItem);
        return order.Id;
    }

    public List<OrderItem> GetAll(OrderItemStatus status = OrderItemStatus.PENDING)
    {
        return [];
    }

    public int RemoveOrderItem(Order order, OrderItem orderItem)
    {
        return order.Id;
    }
}