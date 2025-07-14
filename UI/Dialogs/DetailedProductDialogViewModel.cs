using ReactiveUI;
using ReactiveUI.Fody.Helpers;

namespace Pos.Dialogs;

public class DetailedProductDialogViewModel : ReactiveObject
{
    [Reactive] public string Name { get; set; } = string.Empty;
    [Reactive] public string Description { get; set; } = string.Empty;
    [Reactive] public decimal Price { get; set; }
}
