namespace POS.Models

enum TransactionStatus
{
    PAID,
    PROCESSING,
    FAILED
}
public class Transaction
{
    public int Id { get; set; }
    public Order Order { get; set; }
    public Payment Payment { get; set; }
    public Discount Discount { get; set; }
    public TransactionStatus Status { get; set; }
    public float CreatedAt { get; set; }
}