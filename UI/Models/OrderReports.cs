using System.Collections.ObjectModel;
using System.Text.Json.Serialization;

namespace Pos.Models;

public class OrderReports
{
    [JsonPropertyName("total_orders")]
    public int TotalOrders { get; set; }
    [JsonPropertyName("amount_of_sales")]
    public decimal AmountOfSales { get; set; }
    public ObservableCollection<OrderReport> Orders { get; set; }
}
