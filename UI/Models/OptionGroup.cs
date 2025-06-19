using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Text.Json.Serialization;

namespace Pos.Models
{
    public class OptionGroupBase
    {
        public long? Id { get; set; }
        public string? Name { get; set; }

        [JsonPropertyName("product_id")]
        public long ProductId { get; set; }
    }

    public class OptionGroup : OptionGroupBase
    {
        public int? tableId { get; set; }
        public ObservableCollection<OptionItem> Values { get; set; } = new();
    }

    public class OptionGroupDto : OptionGroupBase
    {
        public List<OptionItem> Values { get; set; } = new();
    }

}