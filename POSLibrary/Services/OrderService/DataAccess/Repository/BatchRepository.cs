using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;

namespace POSLibrary.Services.OrderService.DataAccess.Repository;

public class BatchRepository : IBatchRepository
{
    public Batch Create(List<OrderItem> orderItems)
    {
        throw new NotImplementedException();
    }

    public void UpdateItems(int id, List<OrderItem> orderItems)
    {
        throw new NotImplementedException();
    }

    public List<Batch> Fetch()
    {
        throw new NotImplementedException();
    }

    public Batch? Get(int id)
    {
        throw new NotImplementedException();
    }
}