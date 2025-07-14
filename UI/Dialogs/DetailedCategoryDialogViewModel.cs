using ReactiveUI;
using ReactiveUI.Fody.Helpers;

namespace Pos.Dialogs;

public class DetailedCategoryDialogViewModel : ReactiveObject
{
    [Reactive] public string Name { get; set; } = string.Empty;
}
