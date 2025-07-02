using System.Collections.Generic;
using System.Threading.Tasks;
using Pos.Models;

namespace pos.Api;

public interface IOrderTransactionService
{
    Task<List<OrderTransaction>> GetFilteredOrderTransactionsByStatus(string status, string startDate, string endDate);
}
