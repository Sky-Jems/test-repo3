using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Services.PaymentService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.BillingService.Domain.Entities;

public class Transaction
{
    public int Id { get; set; }
    public required Order Order { get; set; }
    public required Payment Payment { get; set; }
    public Discount? Discount { get; set; }
    public TransactionStatus Status { get; set; }
    public float CreatedAt { get; set; }
}