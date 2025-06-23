using System.Collections.Generic;
using System.Linq;

namespace Pos.Models
{
    public class Product : ProductBase
    {
        public List<Category>? Categories { get; set; }

        public string CategoryNames => Categories != null && Categories.Any()
        ? string.Join(", ", Categories.Select(c => c.Name))
        : string.Empty;
    }

    public class ProductBase
    {
        public long? Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public decimal Price { get; set; }
    }

    public class ProductDto : ProductBase
    {
       public List<long>? Categories { get; set; }
    }
}
