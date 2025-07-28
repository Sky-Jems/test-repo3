using System.Linq;
using System.Text.Json.Serialization;
using ReactiveUI;
using ReactiveUI.Fody.Helpers;

namespace Pos.Models;

public class LineItem : ReactiveObject
{
    public long Id { get; set; }
    public long ProductId { get; set; }
    public string ProductName { get; set; }
    public string ProductDescription { get; set; }
    public Category Category { get; set; }
    [Reactive] public int Quantity { get; set; }
    [Reactive] public decimal Price { get; set; }
    [Reactive] public bool IsSelected { get; set; }
    [Reactive] public decimal ItemTotal { get; set; }
    public DiscountOrderLineitem? Discount { get; set; }
    public decimal? DiscountedAmount => ItemTotal - Discount?.DiscountAmount;
}

public class LineItemDto
{
    [JsonPropertyName("id")]
    public long? Id { get; set; }
    [JsonPropertyName("order_id")]
    public long? OrderId { get; set; }
    [JsonPropertyName("product_id")]
    public long ProductId { get; set; }
    [JsonPropertyName("quantity")]
    public int Quantity { get; set; }
    [JsonPropertyName("price")]
    public decimal Price { get; set; }
}

public static class LineItemMapper
{
    public static LineItemDto ToDto(LineItem lineItem, long? orderId)
    {
        return new LineItemDto
        {
            Id = lineItem.Id,
            OrderId = orderId,
            ProductId = lineItem.ProductId,
            Quantity = lineItem.Quantity,
            Price = lineItem.Price
        };
    }

    public static LineItem FromDto(GetLineItemDto dto)
    {
        var firstCategory = dto.Product.Categories.FirstOrDefault();
        return new LineItem
        {
            Id = dto.Id,
            ProductId = dto.Product.Id,
            ProductName = dto.Product.ProductName,
            ProductDescription = dto.Product.ProductDescription,
            Category = new Category
            {
                Id = firstCategory.Id,
                Name = firstCategory.Name
            },
            Price = dto.Product.Price,
            Quantity = dto.Quantity,
            ItemTotal = dto.SubTotal
        };
    }
}
