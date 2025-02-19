public class Product 
{
    public Guid productId { get; }
    public string sku { get; }
    public string name { get; }
    public float price { get; }
    public string? description { get; }

    public Product(string sku, string name, float price, string? description)
    {
        // Initializes product object
    }
}

public class OptionItem{

    public Guid optionItemId { get; }
    public string name { get; }
    public float price { get; }

    public OptionItem(string name, float price)
    {
        // Initializes OptionItem object
    }
}

public class OptionGroup{
    public string optionGroupId { get; }
    public List<OptionItem> optionItems
    public OptionGroup(string name, List<OptionItem> optionItems)
    {
        // Initializes OptionGroup object
    }
}

public class CartItem
{
    private Guid cartItemId { get; }
    Product product
    private List<OptionGroup> optionGroup
    int quantity { get; }

    public CartItem (Product product, int quantity, List<OptionGroup> optionGroup)
    {
        product = product;
        quantity = quantity;
        options = options;
    }
}

enum OrderStatus
{
    PENDING,
    PROCESSING,
    READY,
    CANCELLED,
    COMPLETE
}

public class Cart
{
    public string customerName;
    public string tableNumber;
    private List<CartItem> _items;
    public List<CartItem> Items 
    {
        get => items;
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

    public Order SaveOrder(List<CartItem> items, string customerName, string tableNumber)
    {
        // Generates `Order` object 
    }

    public void ClearCart()
    {
        // Clears all items in the cart
    }
}

public class Order
{
    public int id;
    public string customerName;
    public string tableNumber;
    private List<CartItem> _items;
    private float _subtotal;
    private float _discounts;
    private OrderStatus _orderStatus;


    public Order()
    {
        // Initializes Order object
    }

    public void UpdateOrderStatus()
    {
        // Updates Order status
    }

    public float Total()
    {
        // Calculate SubTotal price minus the discounts.
    }

    public Bill GenerateBill()
    {
        // Generates Bill to be passed to the Payment service
    }
}


