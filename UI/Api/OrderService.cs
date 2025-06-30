using System;
using System.Threading.Tasks;
using pos.Handlers.Interfaces;
using Pos.Models;

namespace pos.Api;

public class OrderService : IOrderService
{
    private readonly IHttpHandler _httpClient;

    public OrderService(IHttpHandler httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<GetOrderResponseDto> AddOrder(Order order)
    {
        var response = await _httpClient.PostJsonAsync("order-transaction", order);
        return await _httpClient.ReadJsonResponseAsync<GetOrderResponseDto>(response);
    }

    public async Task<GetOrderResponseDto> AddLineItem(LineItemDto lineItemDto)
    {
        var response = await _httpClient.PostJsonAsync("order-transaction/line-item", lineItemDto);
        return await _httpClient.ReadJsonResponseAsync<GetOrderResponseDto>(response);
    }

    public async Task<GetOrderResponseDto> UpdateLineItem(LineItemDto lineItemDto)
    {
        var response = await _httpClient.PutJsonAsync("order-transaction/line-item", lineItemDto);
        return await _httpClient.ReadJsonResponseAsync<GetOrderResponseDto>(response);
    }

    public async Task<GetOrderResponseDto> RemoveLineItem(long lineItemId)
    {
        var response = await _httpClient.DeleteAsync($"order-transaction/line-item/{lineItemId}");
        return await _httpClient.ReadJsonResponseAsync<GetOrderResponseDto>(response);
    }
    
    public async Task<GetOrderResponseDto> ClearLineItems(long orderId)
    {
        var response = await _httpClient.PostJsonAsync($"order-transaction/order/clear-line-items", new { id = orderId });
        return await _httpClient.ReadJsonResponseAsync<GetOrderResponseDto>(response);
    }

    public async Task<OrderResponseDto> PayOrder(long? orderId, string paymentMethod)
    {
        // var rawResponse = await _httpClient.PostJsonAsync("orders/pay", orderId);
        // var response = await _httpClient.ReadJsonResponseAsync<OrderResponse>(rawResponse);
        var guid = Guid.NewGuid();
        var guidBytes = guid.ToByteArray();
        long tempId = BitConverter.ToInt64(guidBytes, 0);
        var responseDto = new OrderResponseDto
        {
            OrderId = tempId,
            Customer = "Customer-" + tempId,
        };
        return responseDto;
    }
}
