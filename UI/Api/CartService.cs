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

public class CartService : ReactiveObject, ICartService
{
     [Reactive] public long? OrderId { get; set; }
     [Reactive] public PaymentStatus PaymentStatus { get; set; } = PaymentStatus.PENDING;
     [Reactive] public string Customer { get; set; } = string.Empty;
     public ObservableCollection<LineItem> Items { get; set; } = new();
     public decimal Total => Items.Sum(lineItem => lineItem.Quantity * lineItem.Price);
     [Reactive] public LineItem? SelectedItem { get; set; }
     public bool CanModifyItems => PaymentStatus == PaymentStatus.PENDING && Payments.Count == 0;
     [Reactive] public decimal AmountPaid { get; set; }
     private decimal? _overrideRemainingBalance;
     public decimal RemainingBalance =>
          _overrideRemainingBalance ?? Total - AmountPaid;

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

     public void ClearItems()
     {
          Items.Clear();
          Customer = string.Empty;
          PaymentStatus = PaymentStatus.PENDING;
          Payments.Clear();
          this.RaisePropertyChanged(nameof(Items));
          this.RaisePropertyChanged(nameof(Total));
          this.RaisePropertyChanged(nameof(PaymentStatus));
          
          ClearRemainingBalanceOverride();
     }

     public void LoadOrder(GetOrderResponseDto orderResponse)
     {
          OrderId = orderResponse.OrderId;
          Customer = orderResponse.Order.Customer;

          Items.Clear();

          foreach (var lineItemDto in orderResponse.Order.LineItems ?? Enumerable.Empty<GetLineItemDto>())
          {
               var lineItem = LineItemMapper.FromDto(lineItemDto);
               Items.Add(lineItem);
          }

          Payments.Clear();
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
          }

          if (orderResponse.Billing != null)
          {
               AmountPaid = orderResponse.Billing.PaidAmount;
               _overrideRemainingBalance = orderResponse.Billing.RemainingAmount;
          }
          else
          {
               AmountPaid = Payments.Sum(p => p.Amount);
               _overrideRemainingBalance = null;
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

          ClearRemainingBalanceOverride();
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
          if (payment.Amount < 0 || payment.OrderId != OrderId) return;

          Payments.Add(payment);
     }

     public void ApplyPayment(PaymentResponseDto paymentResponse)
     {
          if (paymentResponse.OrderId != OrderId) return;

          AmountPaid = paymentResponse.PaidAmount;
          _overrideRemainingBalance = paymentResponse.RemainingAmount;
          this.RaisePropertyChanged(nameof(RemainingBalance));
     }

     private void ClearRemainingBalanceOverride()
     {
          _overrideRemainingBalance = null;
          this.RaisePropertyChanged(nameof(RemainingBalance));
     }
}
