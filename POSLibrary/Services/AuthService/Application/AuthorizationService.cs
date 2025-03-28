namespace POSLibrary.Services.AuthService.Application;

/// <summary>
/// Represents an instance of user authorization service.
/// </summary>
public class AuthorizationService
{
    public AuthorizationService()
    {

    }

    /// <summary>
    /// Check if user has permission accessing on specific resources.
    /// </summary>
    /// <returns>Boolean indicator.</returns>
    public bool HasPermission()
    {
        return true;
    }
}
