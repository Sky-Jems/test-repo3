using System;

namespace pos.Api;

public class UIInteractionService: IUIInteractionService
{
    public event Action<string>? RequestFocus;

    public void FocusCustomerField() => RequestFocus?.Invoke("Customer");
}
