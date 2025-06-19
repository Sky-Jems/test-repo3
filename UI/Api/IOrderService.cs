using System.Collections.Generic;
using System.Threading.Tasks;
using Pos.Models;

namespace pos.Api;

public interface IOrderService
{
 Task<GetOrderResponseDto> AddOrder(Order order);
 Task<GetOrderResponseDto> AddLineItem(LineItemDto lineItemDto);
 Task<OrderResponseDto> RemoveItem(long orderId, long productId);
 Task<OrderResponseDto> PayOrder(long? orderId, string paymentMethod);
}
