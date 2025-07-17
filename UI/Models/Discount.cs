using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;
using System.Text.RegularExpressions;

namespace Pos.Models
{
    public class Discount
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public string Type { get; set; }
        public string DiscountType { get; set; }
        public decimal Value { get; set; }
        public DateTime StartDateTime { get; set; }
        public DateTime EndDateTime { get; set; }
        public decimal MinSpend { get; set; }
        public decimal Cap { get; set; }
        public int MinQty { get; set; }
        public int MaxQty { get; set; }

        public string DisplayValue
        {
            get
            {
                string symbol;
                switch (Type)
                {
                    case "PERCENTAGE":
                        int value = (int)Math.Round(Value, 0);
                        symbol = $"{value}%";
                        break;
                    case "FIXED":
                        symbol = $"₱{Value}";
                        break;
                    default:
                        symbol = "";
                        break;
                }
                return $"{symbol} Off";
            }
        }
    }

    public class DiscountOrderRequest
    {
        [JsonPropertyName("order_id")]
        public long OrderId { get; set; }
        [JsonPropertyName("discount_id")]
        public long? DiscountId { get; set; }
        [JsonPropertyName("total_amount")]
        public decimal TotalAmount { get; set; }
        [JsonPropertyName("line_items")]
        public List<DiscountOrderLineItemRequest>? LineItems { get; set; }
    }

    public class DiscountOrderLineItemRequest
    {
        public long Id { get; set; }
        [JsonPropertyName("line_item_id")]
        public long LineItemId { get; set; }
        [JsonPropertyName("discount_id")]
        public long DiscountId { get; set; }
        [JsonPropertyName("product_id")]
        public long ProductId { get; set; }
        [JsonPropertyName("sub_total")]
        public decimal SubTotal { get; set; }
        public int quantity { get; set; }
        public decimal price { get; set; }
    }

    public class DiscountOrder
    {
        [JsonPropertyName("order_id")]
        public long OrderId { get; set; }
        [JsonPropertyName("discount_amount")]
        public decimal DiscountAmount { get; set; }
        [JsonPropertyName("discount")]
        public DiscountOrderLineitemDiscount? Discount { get; set; }
        [JsonPropertyName("line_items")]
        public List<DiscountOrderLineitem>? lineItems { get; set; }
    }

    public class DiscountOrderLineitem
    {
        [JsonPropertyName("id")]
        public long Id { get; set; }
        [JsonPropertyName("line_item_id")]
        public long LineItemId { get; set; }
        [JsonPropertyName("discount_amount")]
        public decimal DiscountAmount { get; set; }
        [JsonPropertyName("discount")]
        public DiscountOrderLineitemDiscount Discount { get; set; }
    }

    public class DiscountOrderLineitemDiscount
    {
        [JsonPropertyName("id")]
        public long Id { get; set; }
        [JsonPropertyName("name")]
        public string Name { get; set; }
        [JsonPropertyName("type")]
        public string Type { get; set; }
    }
}
