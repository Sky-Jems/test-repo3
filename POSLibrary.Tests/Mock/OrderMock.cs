
using Moq;
using POSLibrary.Services.InventoryService.DataAccess.Repository.Interface;
using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Application;
using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Tests.Mock;

public class MockOrderService
{
    public List<Order> OrdersData = [
        new Order {
            Id = 1,
            CustomerName = "TestUser",
            TableNumber = "Table 1",
            Items = []
        },
        new Order {
            Id = 2,
            CustomerName = "TestUser",
            TableNumber = "Table 2",
            Items = []
        },
        new Order {
            Id = 3,
            CustomerName = "TestUser",
            TableNumber = "Table 3",
            Items = []
        },
    ];
    public List<Order> PendingOrdersData = [
        new Order {
            Id = 1,
            CustomerName = "TestUser",
            TableNumber = "Table 1",
            Items = [
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
                    OrderItemOption = [],
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
                    OrderItemOption = [],
                    Status = OrderItemStatus.PREPARING,
                    Order = new Order { Id = 101, CustomerName = "", TableNumber = "", Items = [] },
                    Ticket = new Ticket { Id = 501, Items = [], TicketNumber = 1 }
                }
            ],
            OrderStatus = Shared.Enums.OrderStatus.PENDING
        },
        new Order {
            Id = 2,
            CustomerName = "TestUser",
            TableNumber = "Table 2",
            Items = [],
            OrderStatus = Shared.Enums.OrderStatus.PENDING
        },
        new Order {
            Id = 3,
            CustomerName = "TestUser",
            TableNumber = "Table 3",
            Items = [],
            OrderStatus = Shared.Enums.OrderStatus.PENDING
        },
    ];

    public OrderService Setup(IOrderItemRepository orderItemRepository, IInventoryRepository inventoryRepository, ITicketRepository ticketRepository)
    {
        var mockOrder = new Mock<IOrderRepository>();

        mockOrder.Setup(orderRepository => orderRepository.CreateOrder(this.OrdersData[0])).Returns(this.OrdersData[0].Id);
        mockOrder.Setup(orderRepository => orderRepository.GetAll(Shared.Enums.OrderStatus.PENDING)).Returns(this.PendingOrdersData);
        mockOrder.Setup(orderRepository => orderRepository.GetOrdersByTableNumber(this.OrdersData[0].TableNumber)).Returns([this.OrdersData[0]]);
        mockOrder.Setup(orderRepository => orderRepository.GetOrderById(this.OrdersData[0].Id)).Returns(this.OrdersData[0]);
        mockOrder.Setup(orderRepository => orderRepository.UpdateOrder(this.OrdersData[0])).Returns(this.OrdersData[0].Id);

        IOrderRepository orderRepository = mockOrder.Object;
        return new(orderRepository, orderItemRepository, inventoryRepository, ticketRepository);
    }
}