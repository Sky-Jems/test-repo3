using System.Linq;
using System.Text.Json.Serialization;
using ReactiveUI;

namespace Pos.Models;

public class LineItem : ReactiveObject
{
    public long Id { get; set; }
    public long ProductId { get; set; }
    public string ProductName { get; set; }
    public string ProductDescription { get; set; }
    public Category Category { get; set; }
    private int _quantity;
    private decimal _price;
    private bool _isSelected;
    public int Quantity
    {
        get => _quantity;
        set
        {
            this.RaiseAndSetIfChanged(ref _quantity, value);
            UpdateItemTotal();
        }
    }
    public decimal Price
    {
        get => _price;
        set
        {
            this.RaiseAndSetIfChanged(ref _price, value);
            UpdateItemTotal();
        }
    }

    public bool IsSelected
    {
        get => _isSelected;
        set => this.RaiseAndSetIfChanged(ref _isSelected, value);
    }
    
    private decimal _itemTotal;
    public decimal ItemTotal
    {
        get => _itemTotal;
        set => this.RaiseAndSetIfChanged(ref _itemTotal, value);
    }
    
    private void UpdateItemTotal()
    {
        ItemTotal = Quantity * Price;
    }
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
    [JsonPropertyName("sub_total")]
    public decimal SubTotal { get; set; }
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
