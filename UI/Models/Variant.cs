using System.Text.Json.Serialization;

namespace Pos.Models;

public class Variant
{
    [JsonPropertyName("variant_id")]
    public long VariantId { get; set; }
    public string Sku { get; set; }
    public double Price { get; set; }
}
