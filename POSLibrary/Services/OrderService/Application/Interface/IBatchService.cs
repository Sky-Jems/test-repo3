using POSLibrary.Services.OrderService.Domain.Entities;

namespace POSLibrary.Services.OrderService.Application.Interface;

public interface IBatchService
{
    public Batch Create(List<OrderItem> orderItems);
    public void UpdateItems(int id, List<OrderItem> orderItems);
    public List<Batch> Fetch();
}
