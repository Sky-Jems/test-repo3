
using Moq;
using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Application;
using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Tests.Mock;

public class MockBatchRepository
{
    public IBatchRepository batchRepository;
    public Mock<IBatchRepository> mock = new();

    public MockBatchRepository()
    {
        mock.Setup(batchRepository => batchRepository.Create(BatchTestData.Data[0].Items)).Returns(BatchTestData.Data[0]);
        mock.Setup(batchRepository => batchRepository.UpdateItems(BatchTestData.Data[0].Id, BatchTestData.Data[0].Items)).Verifiable();
        mock.Setup(batchRepository => batchRepository.Fetch()).Returns(
            BatchTestData.Data.FindAll(batch => batch.Items.Any(item => item.Status == OrderItemStatus.PREPARING))
        );
        mock.Setup(batchRepository => batchRepository.Get(BatchTestData.Data[0].Id)).Returns(BatchTestData.Data[0]);
        this.batchRepository = mock.Object;
    }

    public BatchService SetUp(IOrderItemRepository orderItemRepository)
    {
        return new(this.batchRepository, orderItemRepository);
    }
}