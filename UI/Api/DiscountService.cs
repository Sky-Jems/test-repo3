
using System.Collections.Generic;
using System.Text.Json;
using System.Threading.Tasks;
using pos.Handlers.Interfaces;
using Pos.Models;

namespace pos.Api;

public class DiscountService : IDiscountService
{
    private readonly IHttpHandler _httpClient;

    public DiscountService(IHttpHandler httpHandler)
    {
        _httpClient = httpHandler;
    }

    public async Task<List<Discount>> GetDiscounts()
    {
        return await _httpClient.GetJsonAsync<List<Discount>>("discounts");
    }

    public async Task<GetOrderResponseDto> ApplyDiscount(DiscountOrderRequest discountOrder)
    {
        var response = await _httpClient.PostJsonAsync("order-transaction/apply-discount", discountOrder);
        return await _httpClient.ReadJsonResponseAsync<GetOrderResponseDto>(response);
    }

    public async Task<GetOrderResponseDto> RemoveOrderDiscount(DiscountOrderRequest discountOrder)
    {
        var response = await _httpClient.DeleteAsync("order-transaction/remove-order-discount", discountOrder);
        return await _httpClient.ReadJsonResponseAsync<GetOrderResponseDto>(response);
    }

    public async Task<GetOrderResponseDto> RemoveLineItemDiscount(DiscountOrderRequest discountOrder)
    {
        var response = await _httpClient.DeleteAsync("order-transaction/remove-line-item-discount", discountOrder);
        return await _httpClient.ReadJsonResponseAsync<GetOrderResponseDto>(response);
    }

    public async Task<DiscountOrder> GetDiscountsByOrder(long orderId)
    {
        return await _httpClient.GetJsonAsync<DiscountOrder>($"discount-order/{orderId}");
    }
}