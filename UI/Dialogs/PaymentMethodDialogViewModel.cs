using System.Collections.ObjectModel;
using ReactiveUI;

namespace Pos.Dialogs;

// TODO: replace with actual model
public class PaymentHistory
{
    public string Method { get; set; }
    public decimal Amount { get; set; }
}

public class PaymentMethodDialogViewModel : ReactiveObject
{
    public ObservableCollection<PaymentHistory> PaymentHistoryList { get; set; } = [];

    public PaymentMethodDialogViewModel()
    {
        // TODO: remove after integration
        PaymentHistoryList.Add(new PaymentHistory { Method = "Cash", Amount = 100 });
        PaymentHistoryList.Add(new PaymentHistory { Method = "Card", Amount = 200 });
        PaymentHistoryList.Add(new PaymentHistory { Method = "Gcash", Amount = 300 });
        PaymentHistoryList.Add(new PaymentHistory { Method = "Voucher", Amount = 400 });
    }
}
