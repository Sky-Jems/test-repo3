using System;
using System.Collections.Generic;
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
     [Reactive] public decimal SubTotal { get; set; }
     [Reactive] public decimal DiscountAmount { get; set; }
     [Reactive] public decimal Total { get; set; }
     [Reactive] public LineItem? SelectedItem { get; set; }
     [Reactive] public decimal AmountPaid { get; set; }
     [Reactive] public decimal RemainingBalance { get; set; }
     [Reactive] public decimal Amount { get; set; }
     [Reactive] public string PaymentMethod { get; set; } = "cash";
     [Reactive] public string Notes { get; set; } = string.Empty;
     [Reactive] public DiscountOrder? DiscountOrder { get; set; }

     public ObservableCollection<Payment> Payments { get; set; } = new();
     public ObservableCollection<LineItem> Items { get; set; } = new();
     public List<Discount> Discounts { get; set; } = new();

     public bool CanModifyItems => PaymentStatus == PaymentStatus.PENDING && Payments.Count == 0;

     public void LoadOrder(GetOrderResponseDto orderResponse)
     {
          OrderId = orderResponse.OrderId;
          Customer = orderResponse.Order.Customer;
          DiscountAmount = orderResponse.DiscountAmount;
          DiscountOrder = orderResponse.DiscountOrder;

          if (Enum.TryParse<PaymentStatus>(orderResponse.PaymentStatus, true, out var parsedStatus))
               PaymentStatus = parsedStatus;

          LoadLineItems(orderResponse.Order.LineItems);
          LoadPayments(orderResponse.Payments);
          ApplyBilling(orderResponse.Billing, orderResponse.NetAmount);
          ApplyLineItemDiscounts();

          SubTotal = orderResponse.GrossAmount;
          Total = orderResponse.NetAmount;

          this.RaisePropertyChanged(nameof(Items));
          this.RaisePropertyChanged(nameof(DiscountOrder));
     }

     public void Reset()
     {
          OrderId = null;
          Customer = string.Empty;
          PaymentStatus = PaymentStatus.PENDING;
          DiscountAmount = 0;
          DiscountOrder = null;
          SubTotal = 0;
          Total = 0;

          Items.Clear();
          Payments.Clear();
          Discounts.Clear();
          
          AmountPaid = 0;
          RemainingBalance = 0;

          this.RaisePropertyChanged(nameof(Items));
          this.RaisePropertyChanged(nameof(PaymentStatus));
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
          RemainingBalance = paymentResponse.RemainingAmount;
     }

     public void LoadDiscounts(List<Discount> discounts)
     {
          Discounts = discounts;
          this.RaisePropertyChanged(nameof(Discounts));
     }

     private void LoadLineItems(IEnumerable<GetLineItemDto>? lineItems)
     {
          Items.Clear();
          foreach (var lineItemDto in lineItems ?? Enumerable.Empty<GetLineItemDto>())
          {
               var lineItem = LineItemMapper.FromDto(lineItemDto);
               Items.Add(lineItem);
          }
     }

     private void LoadPayments(IEnumerable<PaymentListResponseDto>? paymentDtos)
     {
          Payments.Clear();
          foreach (var paymentDto in paymentDtos ?? Enumerable.Empty<PaymentListResponseDto>())
          {
               var payment = new Payment
               {
                    OrderId = OrderId.Value,
                    Amount = paymentDto.Amount,
                    PaymentMethod = paymentDto.PaymentMethod,
                    Notes = paymentDto.Notes
               };
               Payments.Add(payment);
          }
     }

     private void ApplyBilling(PaymentResponseDto? billing, decimal netAmount)
     {
          if (billing != null)
          {
               AmountPaid = billing.PaidAmount;
               RemainingBalance = billing.RemainingAmount;
          }
          else
          {
               AmountPaid = 0;
               RemainingBalance = netAmount;
          }
     }

     private void ApplyLineItemDiscounts()
     {
          if (DiscountOrder?.lineItems == null) return;

          foreach (var lineItem in Items)
          {
               lineItem.Discount = DiscountOrder.lineItems.FirstOrDefault(item => item.LineItemId == lineItem.Id);
          }
     }
}
