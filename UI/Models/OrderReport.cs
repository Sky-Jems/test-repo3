using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace Pos.Models;

public class OrderReport
{
    public int Id { get; set; }
    public string Customer { get; set; }
    [JsonPropertyName("table_number")]
    public string TableNumber { get; set; }
    public decimal Total { get; set; }
    [JsonPropertyName("created_at")]
    public string CreatedAt { get; set; }
    [JsonPropertyName("line_items")]
    public List<LineItemDto> LineItems { get; set; }
}
