using System;
using System.Collections.ObjectModel;
using System.Linq;
using Pos.Models;
using ReactiveUI;
using ReactiveUI.Fody.Helpers;

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
     [Reactive] public long? OrderId { get; set; }
     [Reactive] public PaymentStatus PaymentStatus { get; set; } = PaymentStatus.PENDING;
     [Reactive] public string Customer { get; set; } = string.Empty;
     public ObservableCollection<LineItem> Items { get; set; } = new();
     public decimal Total => Items.Sum(lineItem => lineItem.Quantity * lineItem.Price);
     [Reactive] public LineItem? SelectedItem { get; set; }
     public bool CanModifyItems => PaymentStatus == PaymentStatus.PENDING;
     [Reactive] public decimal AmountPaid { get; set; }
     public decimal RemainingBalance => Total - AmountPaid;
     [Reactive] public decimal Amount { get; set; }
     [Reactive] public string PaymentMethod { get; set; } = "cash";
     [Reactive] public string Notes { get; set; } = string.Empty;
     public ObservableCollection<Payment> Payments { get; set; } = new();

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
          
          Payments.Clear();
          AmountPaid = 0;
          foreach (var paymentDto in orderResponse.Payments ?? Enumerable.Empty<PaymentListResponseDto>())
          {
               var payment = new Payment
               {
                    OrderId = orderResponse.OrderId,
                    Amount = paymentDto.Amount,
                    PaymentMethod = paymentDto.PaymentMethod,
                    Notes = paymentDto.Notes
               };
               Payments.Add(payment);
               AmountPaid += payment.Amount;
          }

          this.RaisePropertyChanged(nameof(Items));
          this.RaisePropertyChanged(nameof(Total));
          this.RaisePropertyChanged(nameof(RemainingBalance));
     }

     public void ResetOrder()
     {
          ClearItems();
          OrderId = null;
     }

     public void ResetPayments()
     {
          Payments.Clear();
          AmountPaid = 0;
          this.RaisePropertyChanged(nameof(RemainingBalance));
     }

     public Payment MakePayment()
     {
          return new Payment
          {
               OrderId = OrderId!.Value,
               Amount = Amount,
               PaymentMethod = PaymentMethod,
               Notes = Notes
          };
     }
     
     public void AddPayment(Payment payment)
     {
          if (payment.Amount < 0 || payment.OrderId != OrderId)
               return;

          Payments.Insert(0, payment);
          AmountPaid += payment.Amount;

          this.RaisePropertyChanged(nameof(RemainingBalance));
     }
}
