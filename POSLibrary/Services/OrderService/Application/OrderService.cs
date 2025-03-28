using POSLibrary.Services.InventoryService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Application.Interface;
using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;

using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.OrderService.Application;

public class OrderService(
    IOrderRepository orderRepository,
    IOrderItemRepository orderItemRepository,
    IInventoryRepository inventoryRepository,
    ITicketRepository ticketRepository) : IOrderService
{
    private readonly IOrderRepository _orderRepository = orderRepository;
    private readonly IInventoryRepository _inventoryRepository = inventoryRepository;
    private readonly IOrderItemRepository _orderItemRepository = orderItemRepository;
    private readonly ITicketRepository _ticketRepository = ticketRepository;

    private bool ValidateOrderItems(List<OrderItem> orderItems)
    {
        foreach (var item in orderItems)
        {
            var product = this._inventoryRepository.GetProductById(item.Product.Id);
            if (product == null) return false;

            foreach (var option in item.OrderItemOption)
            {
                var productOption = product.ProductOption.First((_option) =>
                {
                    return _option.ProductId == item.Product.Id
                    && _option.OptionGroupId == option.OptionGroupId
                    && _option.OptionItemId == option.OptionItemId;
                });
                if (productOption == null)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public int CreateOrder(Order order)
    {
        if (!this.ValidateOrderItems(order.Items)) throw new ArgumentException("Order is not valid");
        var orderCreated = this._orderRepository.CreateOrder(order);

        if (orderCreated <= 0)
        {
            var ticket = this._ticketRepository.CreateTicket(order.Items);
        }

        return orderCreated;
    }

    public Order? GetOrderById(int orderId)
    {
        return this._orderRepository.GetOrderById(orderId);
    }

    public List<Order> GetOrdersByTableNumber(string tableNumber)
    {
        return this._orderRepository.GetOrdersByTableNumber(tableNumber);
    }

    public List<Order> GetPendingOrders()
    {
        return this._orderRepository.GetAll();
    }

    public int UpdateOrderItem(Order order, OrderItem updatedOrderItem)
    {
        if (!ValidateOrderItems(new List<OrderItem> { updatedOrderItem })) throw new ArgumentException("Invalid order item");

        var existingItem = order.Items.Find(item => item.Id == updatedOrderItem.Id);
        if (existingItem != null)
        {
            if (existingItem.Status != OrderItemStatus.PENDING)
            {
                throw new ArgumentException("An order item is already being processed.");
            }
            return _orderItemRepository.UpdateOrderItem(existingItem, updatedOrderItem);
        }
        throw new ArgumentException("Order item not found.");
    }

    public int AddOrderItems(Order order, List<OrderItem> additionalOrderItems)
    {
        if (!ValidateOrderItems(additionalOrderItems)) throw new ArgumentException("Invalid order item");
        var orderId = _orderItemRepository.AddOrderItems(order, additionalOrderItems);
        if (orderId <= 0)
        {
            var ticket = _ticketRepository.CreateTicket(additionalOrderItems);
        }
        return orderId;
    }

    public int RemoveOrderItem(Order order, OrderItem orderItem)
    {
        var existingItem = order.Items.Find(item => item.Id == orderItem.Id);
        if (existingItem != null)
        {
            if (existingItem.Status != OrderItemStatus.PENDING)
            {
                throw new ArgumentException($"{orderItem.Product.Name} is already being processed.");
            }

            return _orderItemRepository.RemoveOrderItem(order, existingItem);
        }
        throw new ArgumentException("Order item not found.");
    }

    public int UpdateOrder(Order updatedOrder)
    {
        var existingOrder = this._orderRepository.GetOrderById(updatedOrder.Id);
        if (existingOrder == null) throw new ArgumentException("Order not found.");

        return this._orderRepository.UpdateOrder(updatedOrder);
    }
}