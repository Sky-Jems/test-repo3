using System.Threading.Tasks;
using Pos.Models;

namespace pos.Api;

public interface IOrderReportService
{
    Task<OrderReports> GetOrderReports();
    Task<OrderReports> GetFilteredOrderReports(string startDate, string endDate);
}
