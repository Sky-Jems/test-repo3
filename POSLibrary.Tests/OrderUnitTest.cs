using POSLibrary.Services.InventoryService.Application;
using POSLibrary.Services.OrderService.Application;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Tests.Mock;
using POSLibrary.Tests.Mock.Sample;

namespace POSLibrary.Tests;

public class OrderUnitTest
{
    protected InventoryService InventoryService { get; private set; }
    protected OrderService OrderService { get; private set; }
    private MockInventoryService moqInventory; 
    private MockOrderItemRepository moqOrderItemRepository; 
    private MockOrderService moqOrder; 
    private MockTicketService moqTicket; 

    [SetUp]
    public void Setup()
    {
        this.moqInventory = new MockInventoryService();
        this.InventoryService = this.moqInventory.Setup();

        this.moqTicket = new MockTicketService();
        this.moqTicket.SetUp();

        this.moqOrderItemRepository = new MockOrderItemRepository();
        this.moqOrderItemRepository.SetUp();

        this.moqOrder = new MockOrderService();
        this.OrderService = this.moqOrder.Setup(this.moqOrderItemRepository.orderItemRepository, this.moqInventory.inventoryRepository, this.moqTicket.ticketRepository);
    }

    [Test]
    public void CreateOrderTest()
    {
        int id = this.OrderService.CreateOrder(this.moqOrder.OrdersData[0]);
        Assert.That(actual: id, Is.EqualTo(this.moqOrder.OrdersData[0].Id));
    }

    [Test]
    public void GetOrderByIdTest()
    {
        var result = this.OrderService.GetOrderById(this.moqOrder.OrdersData[0].Id);
        Assert.That(actual: result, Is.Not.Null);
        Assert.Multiple(() =>
        {
            Assert.That(actual: this.moqOrder.OrdersData[0].Id, Is.EqualTo(result.Id));
            Assert.That(actual: this.moqOrder.OrdersData[0].CustomerName, Is.EqualTo(result.CustomerName));
            Assert.That(actual: this.moqOrder.OrdersData[0].TableNumber, Is.EqualTo(result.TableNumber));
        });
    }

    [Test]
    public void GetOrdersByTableNumberTest()
    {
        var orders = this.OrderService.GetOrdersByTableNumber(this.moqOrder.OrdersData[0].TableNumber);
        Assert.That(actual: orders, Has.Count.EqualTo(1));
    }


    [Test]
    public void GetPendingOrdersTest()
    {
        var orders = this.OrderService.GetPendingOrders();
        orders.All(order => order.OrderStatus == Shared.Enums.OrderStatus.PENDING);
        Assert.That(actual: orders, Has.Count.EqualTo(this.moqOrder.PendingOrdersData.Count));
    }

    [Test]
    public void UpdateOrderItemTest()
    {
        int id = this.OrderService.UpdateOrderItem(OrderTestData.PendingOrders[2], OrderTestData.PendingOrders[2].Items[0]);
        Assert.That(actual: id, Is.EqualTo(OrderTestData.PendingOrders[2].Items[0].Id));
    }

    [Test]
    public void AddOrderItemsTest()
    {
        int orderId = OrderService.AddOrderItems(OrderTestData.PendingOrders[0], OrderTestData.PendingOrders[0].Items);
        Assert.That(actual: orderId, Is.EqualTo(OrderTestData.PendingOrders[0].Id));
    }

    [Test]
    public void RemoveOrderItemsTest()
    {
        int orderId = OrderService.RemoveOrderItem(OrderTestData.PendingOrders[2], OrderTestData.PendingOrders[2].Items[0]);
        Assert.That(actual: orderId, Is.EqualTo(OrderTestData.PendingOrders[2].Id));
    }

    [Test]
    public void UpdateOrderTest()
    {
        int id = this.OrderService.UpdateOrder(this.moqOrder.OrdersData[0]);
        Assert.That(actual: id, Is.EqualTo(this.moqOrder.OrdersData[0].Id));
    }
}