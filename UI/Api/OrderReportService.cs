using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
using pos.Handlers;
using pos.Handlers.Interfaces;
using Pos.Models;

namespace pos.Api;

public class OrderReportService : IOrderReportService
{
    private readonly IHttpHandler _httpClient;

    public OrderReportService(IHttpHandler httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<OrderReports> GetOrderReports()
    {
        // var response = await _httpClient.GetJsonAsync<OrderReports>("orders/reports");
        // return response ?? new OrderReports();
        var sampleOrderReports = new OrderReports
        {
            TotalOrders = 2,
            AmountOfSales = 250.00m,
            Orders = new ObservableCollection<OrderReport>
            {
                new OrderReport
                {
                    Id = 101,
                    Customer = "Alice Johnson",
                    TableNumber = "A1",
                    Total = 120.00m,
                    CreatedAt = DateTime.Now.AddHours(-2).ToString("yyyy-MM-dd HH:mm:ss"),
                    LineItems = new List<LineItemDto>
                    {
                        new LineItemDto
                        {
                            ProductId = 1,
                            Quantity = 2,
                            Price = 30.00m
                        },
                        new LineItemDto
                        {
                            ProductId = 2,
                            Quantity = 1,
                            Price = 60.00m
                        }
                    }
                },
                new OrderReport
                {
                    Id = 102,
                    Customer = "Bob Smith",
                    TableNumber = "B3",
                    Total = 130.00m,
                    CreatedAt = DateTime.Now.AddHours(-1).ToString("yyyy-MM-dd HH:mm:ss"),
                    LineItems = new List<LineItemDto>
                    {
                        new LineItemDto
                        {
                            ProductId = 3,
                            Quantity = 1,
                            Price = 130.00m
                        }
                    }
                }
            }
        };
        return sampleOrderReports;
    }

    public async Task<OrderReports> GetFilteredOrderReports(string startDate, string endDate)
    {
        var response = await _httpClient.GetJsonAsync<OrderReports>($"orders/reports?start_date={startDate}&end_date={endDate}");
        return response ?? new OrderReports();
    }
}
