using System.Collections.ObjectModel;
using Pos.Models;
using ReactiveUI;

namespace Pos.Dialogs;

public class DiscountDialogViewModel : ReactiveObject
{
    public ObservableCollection<Discount> DiscountList { get; set; } = [];
}
