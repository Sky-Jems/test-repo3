
namespace Pos.Models
{
    public class Category
    {
        public long? Id { get; set; }

        public string Name { get; set; }

        public string TrimmedCategoryName => Name.Length > 30 ? Name.Substring(0, 30) + "..." : Name;
    }
}