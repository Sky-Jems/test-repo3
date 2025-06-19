using System.Collections.ObjectModel;
using Pos.Models;
using ReactiveUI;

namespace pos.Api;

public interface ICartService
{
    long? OrderId { get; set; }
    PaymentStatus PaymentStatus { get; set; }
    ObservableCollection<LineItem> Items { get; set; }
    LineItem AddItem(LineItem lineItem, bool incrementIfExists = true);
    void RemoveItem(LineItem lineItem);
    void ClearItems();
    decimal Total { get; }
    string CustomerName { get; set; }
    LineItem? SelectedItem { get; set; }
    Order GetSelectedOrder();
    void LoadOrder(GetOrderResponseDto order);
}
