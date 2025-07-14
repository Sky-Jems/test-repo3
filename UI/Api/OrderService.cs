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

    public async Task<PaymentResponseDto> PayOrder(Payment payment)
    {
        var rawResponse = await _httpClient.PostJsonAsync("payment", payment);
        return await _httpClient.ReadJsonResponseAsync<PaymentResponseDto>(rawResponse);
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
