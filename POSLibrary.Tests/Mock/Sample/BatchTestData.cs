using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

public static class BatchTestData
{
    public static List<Batch> Data = [
        new Batch {
            Id = 1,
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
        new Batch {
            Id = 2,
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
        new Batch {
            Id = 3,
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