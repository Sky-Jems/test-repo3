using System;

namespace pos.Api;

public interface IUIInteractionService
{
    event Action<string>? RequestFocus;

    void FocusCustomerField();
}
