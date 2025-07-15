using Pos.Models;
using ReactiveUI;

namespace Pos.Controls;

public class OrderDisplayPanelViewModel : ReactiveObject
{
    public OrderTransaction _OrderTransactionDetails;
    public OrderTransaction OrderTransactionDetails
    {
        get => _OrderTransactionDetails;
        set => this.RaiseAndSetIfChanged(ref _OrderTransactionDetails, value);
    }

    public void SetOrder(OrderTransaction order)
    {
        OrderTransactionDetails = order;
    }
}
