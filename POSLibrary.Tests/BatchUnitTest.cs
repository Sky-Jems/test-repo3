using POSLibrary.Services.OrderService.Application;
using POSLibrary.Shared.Enums;
using POSLibrary.Tests.Mock;

namespace POSLibrary.Tests;

public class BatchUnitTest
{
    protected BatchService BatchService { get; private set; }
    private MockBatchRepository MockBatchRepository;
    private MockOrderItemRepository _mockOrderItemRepository;

    [SetUp]
    public void Setup()
    {
        this._mockOrderItemRepository = new MockOrderItemRepository();
        this._mockOrderItemRepository.SetUp();

        this.MockBatchRepository = new MockBatchRepository();
        this.BatchService = this.MockBatchRepository.SetUp(this._mockOrderItemRepository.orderItemRepository);
    }

    [Test]
    public void CreateTest()
    {
        var batch = this.BatchService.Create(BatchTestData.Data[0].Items);
        Assert.Multiple(() =>
        {
            Assert.That(batch.Id, Is.EqualTo(BatchTestData.Data[0].Id));
            Assert.That(batch.Items, Has.Count.EqualTo(BatchTestData.Data[0].Items.Count));
        });
    }

    [Test]
    public void ExistingItemCreateTest()
    {
        this._mockOrderItemRepository.mock.Setup(orderItemRepository => orderItemRepository.GetAll(OrderItemStatus.PENDING))
        .Returns(BatchTestData.Data[0].Items);
        Assert.That(() => this.BatchService.Create(BatchTestData.Data[0].Items), Throws.ArgumentException);
    }

    [Test]
    public void UpdateItemsTest()
    {
        Assert.That(() => this.BatchService.UpdateItems(BatchTestData.Data[0].Id, BatchTestData.Data[0].Items), Throws.Nothing);
    }

    [Test]
    public void MissingBatchIdUpdateTest()
    {
        Assert.That(() => this.BatchService.UpdateItems(123, BatchTestData.Data[0].Items), Throws.ArgumentException);
    }

    [Test]
    public void FetchTest()
    {
        var batches = this.BatchService.Fetch();
        Assert.That(batches, Has.Count.EqualTo(2));
    }
}