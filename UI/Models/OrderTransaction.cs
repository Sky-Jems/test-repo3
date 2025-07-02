using System.Text.Json.Serialization;

namespace Pos.Models;

public class OrderTransaction
{
    public long Id { get; set; }
    public OrderReport Order { get; set; }
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
