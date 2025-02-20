namespace POS.Models;

public class Cart
{
    public string customerName;
    public string tableNumber;
    private List<CartItem> _items;
    public List<CartItem> Items
    {
        get => _items;
    }

    public Cart() // constructor
    {
        // Set default empty list value on `items`
    }

    public void Add(Guid productId, int quantity, List<OptionGroup> optionGroup)
    {
        // Initializes CartItem from received product and adds to `items`
    }

    public void Update(Guid cartItemId, int newQuantity, List<OptionGroup> optionGroup)
    {
        // Updates CartItem's quantity using the newQuantity in `_items` array
        // If quantity after update would be less than 1, prompt
        // the user if they want to remove the CartItem; do so if yes is
        // selected. Cancel the action if no is selected instead.
    }

    public void Remove(Guid cartItemId)
    {
        // Pops CartItem provided out of `_items` array
    }

    public float SubTotal()
    {
        // Calculates the price multiplied by quantity of each CartItem.
    }

    public Order ToOrder(List<CartItem> items, string customerName, string tableNumber)
    {
        // Generates `Order` object 
    }

    public void Clear()
    {
        // Clears all items in the cart
    }
}

public class CartItem
{
    private Guid cartItemId { get; }
    Product product;
    private List<OptionGroup> optionGroup;
    int quantity;

    public CartItem(Product product, int quantity, List<OptionGroup> optionGroup)
    {
        product = product;
        quantity = quantity;
        options = options;
    }
}