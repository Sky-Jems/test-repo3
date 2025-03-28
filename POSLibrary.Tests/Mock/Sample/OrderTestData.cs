using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Tests.Mock.Sample;

public static class OrderTestData {
    public static List<Order> PendingOrders = [
        new Order {
            Id = 1,
            CustomerName = "TestUser",
            TableNumber = "Table 1",
            Items = [],
            OrderStatus = OrderStatus.PENDING
        },
        new Order {
            Id = 2,
            CustomerName = "TestUser",
            TableNumber = "Table 2",
            Items = [],
            OrderStatus = OrderStatus.PENDING
        },
        new Order {
            Id = 3,
            CustomerName = "TestUser",
            TableNumber = "Table 3",
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
                    OrderItemOption = new List<OrderItemOption>(),
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
            ],
            OrderStatus = OrderStatus.PENDING
        },
    ];
}