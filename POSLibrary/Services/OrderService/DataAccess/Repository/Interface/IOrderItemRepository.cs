using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.DataAccess.Repository.Interface;

public interface IOrderItemRepository
{
    public int UpdateOrderItem(OrderItem existingOrderItem, OrderItem updatedOrderItem);
    public int AddOrderItems(Order order, List<OrderItem> additionalOrderItem);
    public int RemoveOrderItem(Order order, OrderItem orderItem);
    public List<OrderItem> GetAll(OrderItemStatus status = OrderItemStatus.PENDING);
}