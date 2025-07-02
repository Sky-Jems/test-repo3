using System;
using System.Collections.ObjectModel;
using System.Linq;
using Pos.Models;
using ReactiveUI;

namespace pos.Api;

public enum PaymentStatus
{
     PENDING,
     COMPLETED,
     CANCELLED,
     APPROVED,
}

public class CartService: ReactiveObject, ICartService
{
     private long? _orderId;
     public long? OrderId
     {
          get => _orderId;
          set => this.RaiseAndSetIfChanged(ref _orderId, value);
     }

     private PaymentStatus _paymentStatus = PaymentStatus.PENDING;

     public PaymentStatus PaymentStatus
     {
          get => _paymentStatus;
          set => this.RaiseAndSetIfChanged(ref _paymentStatus, value);
     }

     public ObservableCollection<LineItem> Items { get; set; } = new();

     private string _customerName = string.Empty;
     public string Customer
     {
          get => _customerName;
          set => this.RaiseAndSetIfChanged(ref _customerName, value);
     }

     public CartService()
     {
          Items.CollectionChanged += (_, _) =>
          {
               this.RaisePropertyChanged(nameof(Total));
               SubscribeToLineItemChanges();
          };
     }

     private void SubscribeToLineItemChanges()
     {
          foreach (var item in Items)
          {
               item.Changed.Subscribe(_ =>
               {
                    this.RaisePropertyChanged(nameof(Total));
               });
          }
     }

     public LineItem AddItem(LineItem item, bool incrementIfExists = true)
     {
          var existingItem  = Items.FirstOrDefault(lineItem => lineItem.ProductId == item.ProductId);
          if (existingItem == null)
          {
               Items.Insert(0, item);
               return item;
          }

          if (incrementIfExists)
          {
               var oldIndex = Items.IndexOf(existingItem);
               if (oldIndex > 0)
               {
                    Items.Move(oldIndex, 0);
               }
               existingItem.Quantity++;
          }
          this.RaisePropertyChanged(nameof(Total));
          return existingItem;
     }

     public void RemoveItem(LineItem item)
     {
          var existingItem = Items.FirstOrDefault(lineItem => lineItem.ProductId == item.ProductId);
          if (existingItem == null)
               return;

          if (existingItem.Quantity > 1)
          {
               existingItem.Quantity--;
          }
          else
          {
               Items.Remove(existingItem);
          }

          this.RaisePropertyChanged(nameof(Total));
     }

     public void ClearItems()
     {
          Items.Clear();
          Customer = string.Empty;
          this.RaisePropertyChanged(nameof(Items));
          this.RaisePropertyChanged(nameof(Total));
     }

     public decimal Total => Items.Sum(lineItem => lineItem.Quantity * lineItem.Price);

     private LineItem? _selectedItem;
     public LineItem? SelectedItem
     {
          get => _selectedItem;
          set => this.RaiseAndSetIfChanged(ref _selectedItem, value);
     }

     public void LoadOrder(GetOrderResponseDto orderResponse)
     {
          OrderId = orderResponse.OrderId;
          Customer = orderResponse.Order.Customer;

          if (Enum.TryParse<PaymentStatus>(orderResponse.PaymentStatus, true, out var parsedStatus))
          {
               PaymentStatus = parsedStatus;
          }

          Items.Clear();

          foreach (var lineItemDto in orderResponse.Order.LineItems ?? Enumerable.Empty<GetLineItemDto>())
          {
               var lineItem = LineItemMapper.FromDto(lineItemDto);
               Items.Add(lineItem);
          }

          this.RaisePropertyChanged(nameof(Items));
          this.RaisePropertyChanged(nameof(Total));
     }

     public void ResetOrder()
     {
          ClearItems();
          OrderId = null;
     }
     
     public bool CanModifyItems => PaymentStatus == PaymentStatus.PENDING;
}
