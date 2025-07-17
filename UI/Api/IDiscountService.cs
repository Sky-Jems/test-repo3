
using System.Collections.Generic;
using System.Threading.Tasks;
using Pos.Models;

namespace pos.Api;


public interface IDiscountService
{
    Task<List<Discount>> GetDiscounts();
    Task<GetOrderResponseDto> ApplyDiscount(DiscountOrderRequest discountOrder);
    Task<GetOrderResponseDto> RemoveOrderDiscount(DiscountOrderRequest discountOrder);
    Task<GetOrderResponseDto> RemoveLineItemDiscount(DiscountOrderRequest discountOrder);
    Task<DiscountOrder> GetDiscountsByOrder(long orderId);
}