using System.Text.Json.Serialization;

namespace Pos.Models;

public class Payment
{
    [JsonPropertyName("order_id")]
    public long OrderId { get; set; }
    [JsonPropertyName("amount")]
    public decimal Amount { get; set; }
    [JsonPropertyName("payment_method")]
    public string PaymentMethod { get; set; }
    public string Notes { get; set; }
}

public class PaymentListResponseDto
{
    [JsonPropertyName("id")]
    public long Id { get; set; }
    [JsonPropertyName("amount")]
    public decimal Amount { get; set; }
    [JsonPropertyName("status")]
    public string Status { get; set; }
    [JsonPropertyName("notes")]
    public string Notes { get; set; }
    [JsonPropertyName("payment_method")]
    public string PaymentMethod { get; set; }
    [JsonPropertyName("billing_request_id")]
    public long BillingRequestId { get; set; }
}

public class PaymentResponseDto
{
    [JsonPropertyName("id")]
    public long Id { get; set; }
    [JsonPropertyName("net_amount")]
    public decimal NetAmount { get; set; }
    [JsonPropertyName("order_id")]
    public long OrderId { get; set; }
    [JsonPropertyName("paid_method")]
    public decimal PaidAmount { get; set; }
    [JsonPropertyName("remaining_amount")]
    public decimal RemainingAmount { get; set; }
}
