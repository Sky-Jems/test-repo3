namespace POS.Models;

enum OrderStatus
{
    PENDING,
    PROCESSING,
    READY,
    CANCELLED,
    COMPLETE
}
public class Order
{
    private int id;
    private string customerName;
    private string tableNumber;
    private List<CartItem> _items;
    private float _subtotal;
    private float _discounts;
    private OrderStatus _orderStatus;


    public Order()
    {
        // Initializes Order object
    }

    public void UpdateStatus()
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