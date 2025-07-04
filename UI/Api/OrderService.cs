using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using pos.Handlers.Interfaces;
using Pos.Models;
using static Pos.Util.Constants;

namespace pos.Api;

public class OrderService : IOrderService
{
    private readonly IHttpHandler _httpClient;

    public OrderService(IHttpHandler httpClient)
    {
        _httpClient = httpClient;
    }

    public static Dictionary<string, string> OrderStatus = new Dictionary<string, string>
    {
        { OrderStatusType.PENDING.ToString(), "P" },
        { OrderStatusType.COMPLETED.ToString(), "CO" },
        { OrderStatusType.CANCELED.ToString(), "CA" },
    };

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

    public async Task<UpdateOrderDto> PayOrder(long? orderId, string paymentMethod)
    {
        // var rawResponse = await _httpClient.PostJsonAsync("orders/pay", orderId);
        // var response = await _httpClient.ReadJsonResponseAsync<OrderResponse>(rawResponse);
        var guid = Guid.NewGuid();
        var guidBytes = guid.ToByteArray();
        long tempId = BitConverter.ToInt64(guidBytes, 0);
        var responseDto = new UpdateOrderDto
        {
            Id = tempId,
            Customer = "Customer-" + tempId,
        };
        return responseDto;
    }

    public async Task<UpdateOrderDto> UpdateCustomer(UpdateOrderDto updateOrderDto)
    {
        var response = await _httpClient.PutJsonAsync($"orders/{updateOrderDto.Id}", updateOrderDto);
        return await _httpClient.ReadJsonResponseAsync<UpdateOrderDto>(response);
    }

    public async Task<GetOrderResponseDto> GetOrderTransaction(long id)
    {
        return await _httpClient.GetJsonAsync<GetOrderResponseDto>($"order-transaction/{id}");
    }
}
