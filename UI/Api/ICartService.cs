using System.Collections.Generic;
using System.Collections.ObjectModel;
using Pos.Models;

namespace pos.Api;

public interface ICartService
{
    long? OrderId { get; set; }
    PaymentStatus PaymentStatus { get; set; }
    string Customer { get; set; }
    ObservableCollection<LineItem> Items { get; set; }
    decimal Total { get; }
    decimal SubTotal { get; }
    LineItem? SelectedItem { get; set; }
    bool CanModifyItems { get; }
    decimal AmountPaid { get; set; }
    decimal RemainingBalance { get; }
    decimal Amount { get; set; }
    decimal DiscountAmount { get; set; }
    string PaymentMethod { get; set; }
    string Notes { get; set; }
    ObservableCollection<Payment> Payments { get; set; }
    DiscountOrder? DiscountOrder { get; set; }
    List<Discount> Discounts { get; set; }
    void LoadOrder(GetOrderResponseDto order);
    void Reset();
    Payment MakePayment();
    void AddPayment(Payment payment);
    void LoadDiscounts(List<Discount> discounts);
    void ApplyPayment(PaymentResponseDto paymentResponseDto);
}
