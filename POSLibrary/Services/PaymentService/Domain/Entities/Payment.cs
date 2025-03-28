using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.PaymentService.Domain.Entities;

public class Payment
{
    public int Id { get; set; }
    public float Amount { get; set; }
    public PaymentType PaymentMethod { get; set; }
    public DateTime PaidAt { get; set; }
}
