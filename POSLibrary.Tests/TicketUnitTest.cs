using POSLibrary.Services.OrderService.Application;
using POSLibrary.Services.OrderService.Application.Interface;
using POSLibrary.Shared.Enums;
using POSLibrary.Tests.Mock;
using POSLibrary.Tests.Mock.Sample;

namespace POSLibrary.Tests;

public class TicketUnitTest
{
    protected ITicketService TicketService { get; private set; }
    private MockTicketService MockTicketRepository;

    [SetUp]
    public void Setup()
    {
        this.MockTicketRepository = new MockTicketService();
        this.TicketService = this.MockTicketRepository.SetUp();
    }

    [Test]
    public void CreateTicketTest()
    {
        var ticket = this.TicketService.CreateTicket(1, TicketTestData.Data[0].Items);
        Assert.That(ticket, Is.Not.Null);
    }

    [Test]
    public void GetAllTicketsTest()
    {
        var tickets = this.TicketService.GetTickets();
        Assert.That(tickets, Has.Count.EqualTo(3));
    }

    [Test]
    public void GetTicketsByStatusTest()
    {
        var tickets = this.TicketService.GetTickets(OrderItemStatus.READY);
        Assert.That(tickets, Has.Count.EqualTo(2));
    }

    [Test]
    public void GetTicketTest()
    {
        var ticket = this.TicketService.GetTicket(TicketTestData.Data[0].Id);
        Assert.That(ticket, Is.EqualTo(TicketTestData.Data[0]));
    }

    [Test]
    public void UpdateTicketTest()
    {
        Assert.That(() => this.TicketService.UpdateTicket(TicketTestData.Data[0].Items[0].Id, TicketTestData.Data[0].Items), Throws.Nothing);
    }

    [Test]
    public void UpdateMissingTicketTest()
    {
        Assert.That(() => this.TicketService.UpdateTicket(123, TicketTestData.Data[0].Items), Throws.ArgumentException);
    }

    [Test]
    public void UpdateTicketItemsTest()
    {
        Assert.That(() => this.TicketService.UpdateTicketItem(TicketTestData.Data[0].Items[0].Id, TicketTestData.Data[0].Items[0]), Throws.Nothing);
    }

    [Test]
    public void RemoveTicketItemTest()
    {
        Assert.That(() => this.TicketService.RemoveTicketItem(TicketTestData.Data[0].Id, TicketTestData.Data[0].Items[0].Id), Throws.Nothing);
    }
}