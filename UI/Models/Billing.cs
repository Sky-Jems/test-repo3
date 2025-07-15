using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace Pos.Models;

public class Billing
{
    public long Id { get; set; }
    [JsonPropertyName("net_amount")]
    public decimal NetAmount { get; set; }
    [JsonPropertyName("order_id")]
    public long OrderId { get; set; }
    [JsonPropertyName("paid_amount")]
    public decimal PaidAmount { get; set; }
    [JsonPropertyName("remaining_amount")]
    public decimal RemainingAmount { get; set; }
}