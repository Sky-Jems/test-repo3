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
    string Customer { get; set; }
    LineItem? SelectedItem { get; set; }
    void LoadOrder(GetOrderResponseDto order);
    void ResetOrder();
}
