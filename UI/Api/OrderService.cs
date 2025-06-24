using System;
using System.Collections.Generic;
using System.Text.Json;
using System.Threading.Tasks;
using pos.Handlers;
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
        // var response = await _httpClient.PostJsonAsync("order-transaction", order);
        // return await _httpClient.ReadJsonResponseAsync<GetOrderResponseDto>(response);
        var guid = Guid.NewGuid();
        var guidBytes = guid.ToByteArray();
        long tempId = BitConverter.ToInt64(guidBytes, 0);
        var responseDto = new GetOrderResponseDto
        {
            Id = tempId,
            OrderId = tempId,
            Order = new OrderDto
            {
                Id = tempId
            }
        };
        return responseDto;
    }

    public async Task<GetOrderResponseDto> AddLineItem(LineItemDto lineItemDto)
    {
        // var response = await _httpClient.PostJsonAsync("order-transaction/line-item", lineItemDto);
        // return await _httpClient.ReadJsonResponseAsync<GetOrderResponseDto>(response);
        var guid = Guid.NewGuid();
        var guidBytes = guid.ToByteArray();
        long tempId = BitConverter.ToInt64(guidBytes, 0);
        var responseDto = new GetOrderResponseDto
        {
            Id = tempId,
            OrderId = tempId,
            Order = new OrderDto
            {
                Id = tempId
            }
        };
        return responseDto;
    }

    public async Task<OrderResponseDto> RemoveItem(long orderId, long productId)
    {
        // var rawResponse = await _httpClient.DeleteAsync($"orders/{orderId}/{productId}");
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
