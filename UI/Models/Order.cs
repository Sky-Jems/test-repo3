using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace Pos.Models;

public class Order
{
    [JsonPropertyName("customer")]
    public string Customer { get; set; }

    [JsonPropertyName("table_number")]
    public int? TableNumber { get; set; }

    [JsonPropertyName("order_id")]
    public long OrderId { get; set; }

    [JsonPropertyName("line_items")]
    public List<LineItemDto>? LineItems { get; set; }
}

public class OrderResponseDto
{
    [JsonPropertyName("order_id")]
    public long OrderId { get; set; }

    public string Customer { get; set; }

    [JsonPropertyName("line_items")]
    public List<LineItemDto>? LineItems { get; set; }
}

public class GetOrderResponseDto
{
    public long Id { get; set; }
    [JsonPropertyName("order")]

    public OrderDto Order { get; set; }

    [JsonPropertyName("order_id")]
    public long OrderId { get; set; }

    [JsonPropertyName("order_status")]
    public string OrderStatus { get; set; }

    [JsonPropertyName("discount_status")]
    public string DiscountStatus { get; set; }

    [JsonPropertyName("payment_status")]
    public string PaymentStatus { get; set; }

    [JsonPropertyName("gross_amount")]
    public decimal GrossAmount { get; set; }

    [JsonPropertyName("discount_amount")]
    public decimal DiscountAmount { get; set; }

    [JsonPropertyName("net_amount")]
    public decimal NetAmount { get; set; }
}

public class OrderDto
{
    public long Id { get; set; }

    public string Customer { get; set; }

    [JsonPropertyName("table_number")]
    public string TableNumber { get; set; }

    public decimal Total { get; set; }

    [JsonPropertyName("line_items")]
    public List<GetLineItemDto>? LineItems { get; set; }
}

public class GetLineItemDto
{
    public long Id { get; set; }
    [JsonPropertyName("product_id")]
    public long ProductId { get; set; }
    [JsonPropertyName("name")]
    public string ProductName { get; set; }
    [JsonPropertyName("description")]
    public string ProductDescription { get; set; }
    [JsonPropertyName("quantity")]
    public int Quantity { get; set; }
    [JsonPropertyName("price")]
    public decimal Price { get; set; }
    [JsonPropertyName("sub_total")]
    public decimal SubTotal { get; set; }
}
