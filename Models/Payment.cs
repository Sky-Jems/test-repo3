namespace POS.Models;

enum PaymentType
{
    CASH,
    CREDIT_CARD
}
public class Payment
{
    public int Id { get; set; }
    public float Amount { get; set; }
    public PaymentType PaymentMethod { get; set; }
    public DateTime PaidAt { get; set; }
}
