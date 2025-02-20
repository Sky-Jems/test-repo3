namespace POS.Models;

public class Product
{
    public Guid productId { get; }
    public string sku { get; }
    public string name { get; }
    public float price { get; }
    public string? description { get; }
    public Category category { get; }

    public Product(string sku, string name, float price, string? description)
    {
        // Initializes product object
    }
}

public class OptionItem
{

    public Guid optionItemId { get; }
    public string name { get; }
    public float price { get; }

    public OptionItem(string name, float price)
    {
        // Initializes OptionItem object
    }
}

public class OptionGroup
{
    public string optionGroupId { get; }
    public List<OptionItem> optionItems;
    public OptionGroup(List<OptionItem> optionItems)
    {
        // Initializes OptionGroup object
    }
}
