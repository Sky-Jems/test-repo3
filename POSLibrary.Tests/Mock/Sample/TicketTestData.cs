using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Tests.Mock.Sample;

public static class TicketTestData {
    public static List<Ticket> Data = [
        new Ticket {
            Id = 1,
            TicketNumber = 1,
            Items = [
                new OrderItem {
                    Id = 1,
                    Product = new Product {
                        Id = 1,
                        Sku = "Sku-123",
                        Name = "Pasta",
                        Price = 100,
                        Category = new Category
                        {
                            Name = "Pasta",
                            Type = "Food"
                        },
                    },
                    Quantity = 1,
                    Status = OrderItemStatus.READY,
                },
                new OrderItem {
                    Id = 1,
                    Product = new Product {
                        Id = 1,
                        Sku = "Sku-456",
                        Name = "Regular burger",
                        Price = 100,
                        Category = new Category
                        {
                            Name = "Burger",
                            Type = "Food"
                        },
                    },
                    Quantity = 1,
                    Status = OrderItemStatus.READY,
                }
            ],
        },
        new Ticket {
            Id = 2,
            TicketNumber = 2,
            Items = [
                new OrderItem {
                    Id = 1,
                    Product = new Product {
                        Id = 1,
                        Sku = "Sku-123",
                        Name = "Pasta",
                        Price = 100,
                        Category = new Category
                        {
                            Name = "Pasta",
                            Type = "Food"
                        },
                    },
                    Quantity = 1,
                    Status = OrderItemStatus.PREPARING,
                },
                new OrderItem {
                    Id = 1,
                    Product = new Product {
                        Id = 1,
                        Sku = "Sku-456",
                        Name = "Regular burger",
                        Price = 100,
                        Category = new Category
                        {
                            Name = "Burger",
                            Type = "Food"
                        },
                    },
                    Quantity = 1,
                    Status = OrderItemStatus.READY,
                }
            ],
        },
        new Ticket {
            Id = 3,
            TicketNumber = 3,
            Items = [
                new OrderItem {
                    Id = 1,
                    Product = new Product {
                        Id = 1,
                        Sku = "Sku-123",
                        Name = "Pasta",
                        Price = 100,
                        Category = new Category
                        {
                            Name = "Pasta",
                            Type = "Food"
                        },
                    },
                    Quantity = 1,
                    Status = OrderItemStatus.PREPARING,
                },
                new OrderItem {
                    Id = 1,
                    Product = new Product {
                        Id = 1,
                        Sku = "Sku-456",
                        Name = "Regular burger",
                        Price = 100,
                        Category = new Category
                        {
                            Name = "Burger",
                            Type = "Food"
                        },
                    },
                    Quantity = 1,
                    Status = OrderItemStatus.PREPARING,
                }
            ],
        },
    ];
}