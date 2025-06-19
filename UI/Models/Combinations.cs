
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text.Json.Serialization;

namespace Pos.Models
{
    public class Combinations
    {
        [JsonPropertyName("variant_id")]
        public int VariantId { get; set; }
        public Product Product { get; set; }
        [JsonPropertyName("variant_combinations")]
        public List<VariantCombination> VariantCombinations { get; set; }
        public string SKU { get; set; }
        public double? Price { get; set; }
        public string ProductName => Product?.Name ?? "Unnamed";
        public List<Category>? Categories => Product?.Categories;
        public string CategoryNames => Categories != null && Categories.Any()
            ? string.Join(", ", Categories.Select(c => c.Name))
            : "Uncategorized";
        public string VariantName { get; set; }
        public string VariantOptions { get; set; }
        public string VariantSummary { get; set; }

    }

    public class ComboBoxVariants
    {
        public string VariantName { get; set; }
        public ObservableCollection<OptionItem> VariantValues { get; set; }
    }
    public class VariantCombination
    {
        public int Id { get; set; }
        public string Value { get; set; }
        public int VariantOptionId { get; set; }
        public VariantOption VariantOption { get; set; }
    }

    public class VariantOption
    {
        public int Id { get; set; }
        public int ProductId { get; set; }
        public string Name { get; set; }
    }
}