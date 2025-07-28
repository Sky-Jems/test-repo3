
namespace Pos.Models
{
    public class Category
    {
        public long? Id { get; set; }
        public string Name { get; set; }
    }

    public class GetCategoryResponseDto
    {
        public long? Id { get; set; }
        public string Name { get; set; }
    }
}