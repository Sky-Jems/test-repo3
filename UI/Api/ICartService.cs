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
    LineItem? SelectedItem { get; set; }
    bool CanModifyItems { get; }
    decimal AmountPaid { get; set; }
    decimal RemainingBalance { get; }
    decimal Amount { get; set; }
    string PaymentMethod { get; set; }
    string Notes { get; set; }
    ObservableCollection<Payment> Payments { get; set; }
    LineItem AddItem(LineItem lineItem, bool incrementIfExists = true);
    void RemoveItem(LineItem lineItem);
    void ClearItems();
    void LoadOrder(GetOrderResponseDto order);
    void ResetOrder();
    void ResetPayments();
    Payment MakePayment();
    void AddPayment(Payment payment);
}
