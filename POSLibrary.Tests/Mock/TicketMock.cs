
using Moq;
using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Application;
using POSLibrary.Services.OrderService.DataAccess.Repository.Interface;
using POSLibrary.Services.OrderService.Domain.Entities;
using POSLibrary.Shared.Enums;
using POSLibrary.Tests.Mock.Sample;

namespace POSLibrary.Tests.Mock;

public class MockTicketService
{
    public ITicketRepository ticketRepository;

    public MockTicketService()
    {
        var mock = new Mock<ITicketRepository>();

        // mock.Setup(ticketRepository => ticketRepository.CreateTicket(It.IsAny<List<OrderItem>>)).Returns(TicketTestData.Data[0]);
        mock.Setup(ticketRepository => ticketRepository.CreateTicket(TicketTestData.Data[0].Items)).Returns(TicketTestData.Data[0]);
        mock.Setup(ticketRepository => ticketRepository.GetTickets(null)).Returns(TicketTestData.Data);
        mock.Setup(ticketRepository => ticketRepository.GetTickets(OrderItemStatus.READY)).Returns(
            TicketTestData.Data.FindAll(ticket => ticket.Items.Any(item => item.Status == OrderItemStatus.READY))
        );
        mock.Setup(ticketRepository => ticketRepository.GetTicket(TicketTestData.Data[0].Id)).Returns(TicketTestData.Data[0]);
        mock.Setup(ticketRepository => ticketRepository.UpdateItems(TicketTestData.Data[0].Id, TicketTestData.Data[0].Items)).Verifiable();
        mock.Setup(ticketRepository => ticketRepository.UpdateTicketItem(TicketTestData.Data[0].Id, TicketTestData.Data[0].Items[0])).Verifiable();

        this.ticketRepository = mock.Object;
    }

    public TicketService SetUp()
    {
        return new(this.ticketRepository);
    }
}