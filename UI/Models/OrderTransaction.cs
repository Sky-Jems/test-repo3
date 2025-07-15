using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;

namespace Pos.Models;

public class OrderTransaction
{
    public long Id { get; set; }
    public OrderReport Order { get; set; }
    [JsonPropertyName("billing")]
    public Billing? Billing { get; set; }
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
    [JsonPropertyName("payments")]
    public List<Payment?> Payments { get; set; }
    public string PaymentMethodsDisplay =>
        Payments != null && Payments.Any()
            ? string.Join(", ",
                Payments
                    .Select(p =>
                        !string.IsNullOrWhiteSpace(p.PaymentMethod)
                            ? char.ToUpper(p.PaymentMethod[0]) + p.PaymentMethod.Substring(1).ToLower()
                            : string.Empty
                    )
                    .Distinct()
            )
            : string.Empty;
   public decimal SafeRemainingAmount => Billing?.RemainingAmount ?? NetAmount;
   public decimal SafePaidAmount => Billing?.PaidAmount ?? 0.0m;
}