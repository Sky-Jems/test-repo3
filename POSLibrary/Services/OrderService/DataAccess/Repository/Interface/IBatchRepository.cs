using POSLibrary.Services.OrderService.Domain.Entities;

namespace POSLibrary.Services.OrderService.DataAccess.Repository.Interface;

public interface IBatchRepository 
{
    public Batch Create(List<OrderItem> orderItems);
    public void UpdateItems(int id, List<OrderItem> orderItems);
    public List<Batch> Fetch();
    public Batch? Get(int id);
}