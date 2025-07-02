using System.Collections.Generic;
using System.Threading.Tasks;
using pos.Handlers.Interfaces;
using Pos.Models;

namespace pos.Api;

public class OrderTransactionService : IOrderTransactionService
{
    private readonly IHttpHandler _httpClient;

    public OrderTransactionService(IHttpHandler httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<List<OrderTransaction>> GetFilteredOrderTransactionsByStatus(string status, string startDate, string endDate)
    {
        return await _httpClient.GetJsonAsync<List<OrderTransaction>>($"order-transaction/orders?order_status={status}&start_date={startDate}&end_date={endDate}");
    }
}
