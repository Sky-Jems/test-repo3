using System.Threading.Tasks;
using Pos.Models;

namespace pos.Api;

public interface IOrderService
{
 Task<GetOrderResponseDto> AddOrder(Order order);
 Task<GetOrderResponseDto> AddLineItem(LineItemDto lineItemDto);
 Task<GetOrderResponseDto> UpdateLineItem(LineItemDto lineItemDto);
 Task<GetOrderResponseDto> RemoveLineItem(long productId);
 Task<GetOrderResponseDto> ClearLineItems(long orderId);
 Task<PaymentResponseDto> PayOrder(Payment payment);
 Task<UpdateOrderDto> UpdateCustomer(UpdateOrderDto updateOrderDto);
 Task<GetOrderResponseDto> GetOrderTransaction(long id);
}
