using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive.Linq;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using Pos.Models;
using ReactiveUI;
using ReactiveUI.Fody.Helpers;

namespace Pos.Dialogs;

public class DiscountDialogViewModel : ReactiveObject
{
    public ObservableCollection<Discount> DiscountList { get; set; } = [];
    private readonly ICartService _cartService;
    private readonly IOrderService _orderService;
    private readonly IDiscountService _discountService;
    private readonly long? _selectedLineItemId;
    [Reactive] public long AppliedDiscountId { get; set; }

    public bool _isManagingDiscount;

    public DiscountDialogViewModel(long? selectedLineItemId)
    {
        _selectedLineItemId = selectedLineItemId;

        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        _orderService = ServiceLocator.Services.GetRequiredService<IOrderService>();
        _discountService = ServiceLocator.Services.GetRequiredService<IDiscountService>();
    }

    public async Task GetDiscounts()
    {
        List<Discount> discounts = await _discountService.GetDiscounts();
        _cartService.LoadDiscounts(discounts);
        DiscountList.Clear();
        foreach (var discount in discounts)
            DiscountList.Add(discount);

        DiscountOrder? discountOrder = _cartService.DiscountOrder;
        if (discountOrder?.Discount?.Id != null || discountOrder?.lineItems != null)
        {
            AppliedDiscountId = discountOrder.lineItems != null
                ? discountOrder.lineItems.FirstOrDefault(lineItem => lineItem.LineItemId == _selectedLineItemId)?.Discount.Id ?? 0
                : discountOrder.Discount!.Id;
        }
    }

    public async Task ApplyDiscount(Discount discount)
    {
        try
        {
            _isManagingDiscount = true;

            DiscountOrderRequest discountOrderRequest = new()
            {
                OrderId = (long)_cartService.OrderId!,
                DiscountId = discount.Id,
                TotalAmount = _cartService.Total
            };

            if (_selectedLineItemId != null && _cartService.Items.Single(item => item.Id == _selectedLineItemId) != null)
            {
                LineItem selectedItem = _cartService.Items.Single(item => item.Id == _selectedLineItemId);
                discountOrderRequest.DiscountId = null;

                DiscountOrderLineItemRequest orderLineItem = new()
                {
                    LineItemId = selectedItem.Id,
                    DiscountId = discount.Id,
                    ProductId = selectedItem.ProductId,
                    SubTotal = selectedItem.ItemTotal,
                    quantity = selectedItem.Quantity,
                    price = selectedItem.Price
                };
                discountOrderRequest.LineItems = [];
                discountOrderRequest.LineItems.Add(orderLineItem);
            }
            GetOrderResponseDto orderResponseDto = await _discountService.ApplyDiscount(discountOrderRequest);
            _cartService.LoadOrder(orderResponseDto);
        }
        finally
        {
            _isManagingDiscount = false;
        }
    }

    public async Task RemoveDiscount(Discount discount)
    {
        try
        {
            _isManagingDiscount = true;

            DiscountOrderRequest discountOrderRequest = new()
            {
                OrderId = (long)_cartService.OrderId!,
                DiscountId = discount.Id,
                TotalAmount = _cartService.Total
            };

            GetOrderResponseDto orderResponseDto;
            if (_selectedLineItemId != null && _cartService.Items.Single(item => item.Id == _selectedLineItemId) != null)
            {
                LineItem selectedItem = _cartService.Items.Single(item => item.Id == _selectedLineItemId);
                discountOrderRequest.DiscountId = null;

                DiscountOrderLineItemRequest orderLineItem = new()
                {
                    LineItemId = selectedItem.Id,
                    DiscountId = discount.Id,
                    ProductId = selectedItem.ProductId,
                    SubTotal = selectedItem.ItemTotal,
                    quantity = selectedItem.Quantity,
                    price = selectedItem.Price
                };
                discountOrderRequest.LineItems = [];
                discountOrderRequest.LineItems.Add(orderLineItem);
                orderResponseDto = await _discountService.RemoveLineItemDiscount(discountOrderRequest);
            }
            else
                orderResponseDto = await _discountService.RemoveOrderDiscount(discountOrderRequest);

            _cartService.LoadOrder(orderResponseDto);
        }
        finally
        {
            _isManagingDiscount = false;
        }
    }

    public bool CanApplyDiscount()
    {
        if (_selectedLineItemId == null)
        {
            return _cartService?.DiscountOrder?.Discount?.Id == null;
        }
        return _cartService?.DiscountOrder?.lineItems?.FirstOrDefault(lineItem => lineItem.LineItemId == _selectedLineItemId) == null;
    }
}
