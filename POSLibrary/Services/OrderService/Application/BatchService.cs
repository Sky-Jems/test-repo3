using POSLibrary.Services.OrderService.Application.Interface;
using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;

namespace POSLibrary.Services.OrderService.Application;

public class BatchService(IBatchRepository batchRepository, IOrderItemRepository orderItemRepository) : IBatchService
{
    private readonly IBatchRepository _batchRepository = batchRepository;
    private readonly IOrderItemRepository _orderItemRepository = orderItemRepository;

    public Batch Create(List<OrderItem> orderItems)
    {
        var pendingOrderItem = this._orderItemRepository.GetAll();
        foreach (var item in pendingOrderItem)
        {
            var exist = orderItems.Find((value) => value.Id == item.Id);
            if (exist != null)
            {
                throw new ArgumentException($"Order item id: {item.Id} already exist in batch board");
            }
        }
        return this._batchRepository.Create(orderItems);
    }

    public void UpdateItems(int id, List<OrderItem> orderItems)
    {
        var batch = this._batchRepository.Get(id);
        if (batch == null)
        {
            throw new ArgumentException($"Batch id: {id} does not exist in batch board");
        }
        this._batchRepository.UpdateItems(id, orderItems);
    }

    public List<Batch> Fetch()
    {
        return this._batchRepository.Fetch();
    }
}
