using System.Text.Json.Serialization;
using ReactiveUI;

namespace Pos.Models;

public class LineItem : ReactiveObject
{
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
            OrderId = orderId,
            ProductId = lineItem.ProductId,
            Quantity = lineItem.Quantity,
            Price = lineItem.Price
        };
    }
    
    public static LineItem FromDto(GetLineItemDto dto)
    {
        return new LineItem
        {
            ProductId = dto.ProductId,
            ProductName = dto.ProductName,
            Price = dto.Price,
            Quantity = dto.Quantity,
            ItemTotal = dto.SubTotal
        };
    }
}
