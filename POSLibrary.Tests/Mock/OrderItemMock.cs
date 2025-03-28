using Moq;
using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;
using POSLibrary.Tests.Mock.Sample;

namespace POSLibrary.Tests.Mock;

public class MockOrderItemRepository
{
    public IOrderItemRepository orderItemRepository;
    public Mock<IOrderItemRepository>mock = new();

    public List<OrderItem> OrderItems = [
        new OrderItem {
            Id = 1,
            Product = new Product {
                Id = 1,
                Sku = "P001",
                Name = "Burger",
                Price = 5,
                Category = new Category
                    { Id = 1, Name = "Food", Type = "" }
            },
            Quantity = 2,
            OrderItemOption =
            [
                new() { Id = 1, OrderItemId = 1, OptionGroupId = 1, OptionItemId = 1 }, // Example: Extra Cheese
                new() { Id = 2, OrderItemId = 1, OptionGroupId = 2, OptionItemId = 3 }  // Example: Spicy Sauce
            ],
            Status = OrderItemStatus.PENDING,
            Order = new Order { Id = 101, CustomerName = "", TableNumber = "", Items = [] },
            Ticket = new Ticket { Id = 501, Items = [], TicketNumber = 1 }
        },
        new OrderItem
        {
            Id = 2,
            Product = new Product {
                Id = 2,
                Sku = "P002",
                Name = "Pizza",
                Price = 8,
                Category = new Category
                    { Id = 1, Name = "Food", Type = "" }
            },
            Quantity = 1,
            OrderItemOption = new List<OrderItemOption>
            {
                new OrderItemOption { Id = 3, OrderItemId = 2, OptionGroupId = 1, OptionItemId = 2 } // Example: Extra Pepperoni
            },
            Status = OrderItemStatus.PREPARING,
            Order = new Order { Id = 101, CustomerName = "", TableNumber = "", Items = [] },
            Ticket = new Ticket { Id = 501, Items = [], TicketNumber = 1 }
        }
    ];

    public IOrderItemRepository SetUp()
    {
        mock.Setup(orderItemRepository => orderItemRepository.AddOrderItems(OrderTestData.PendingOrders[0], OrderTestData.PendingOrders[0].Items)).Returns(OrderTestData.PendingOrders[0].Id);
        mock.Setup(orderItemRepository => orderItemRepository.UpdateOrderItem(OrderTestData.PendingOrders[2].Items[0], OrderTestData.PendingOrders[2].Items[0])).Returns(OrderTestData.PendingOrders[2].Items[0].Id);
        mock.Setup(orderItemRepository => orderItemRepository.GetAll(OrderItemStatus.PENDING)).Returns([]);
        mock.Setup(orderItemRepository => orderItemRepository.RemoveOrderItem(OrderTestData.PendingOrders[2], OrderTestData.PendingOrders[2].Items[0])).Returns(OrderTestData.PendingOrders[2].Id);

        this.orderItemRepository = mock.Object;
        return orderItemRepository;
    }
}