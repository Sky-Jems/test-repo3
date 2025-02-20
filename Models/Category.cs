namespace POS.Models;

public class Category
{
    public int id { get; }
    public string name { get; }
    public string icon { get; }
    public string type { get; }
    public List<Product> Products { get; } // to provide relationship to Product
    public Category(string name, string icon, string type)
    {
        // Initializes properties
    }
}