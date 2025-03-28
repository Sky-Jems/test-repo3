using POSLibrary.Shared.Common.DTO;
using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.AuthService.Application;

/// <summary>
/// Represents a service for authenticating user.
/// </summary>
public class AuthenticationService
{
    public AuthenticationService()
    {

    }

    /// <summary>
    /// Check and validate user credentials.
    /// </summary>
    /// <param name="username">username of the current user.</param>
    /// <param name="password">password of the current user.</param>
    /// <returns>An instance of user.</returns>
    public UserDto SignIn(string username, string password)
    {
        return new UserDto
        {
            Username = username,
        };
    }

    /// <summary>
    /// Register new user.
    /// </summary>
    /// <param name="username">username of the current user.</param>
    /// <param name="password">password of the current user.</param>
    /// <param name="role">role of the current user.</param>
    /// <returns>An instance newly registered user.</returns>
    public UserDto SignUp(string username, string password, UserRole role)
    {
        return new UserDto
        {
            Username = username,
        };
    }

    /// <summary>
    /// Signout user.
    /// </summary>
    /// <returns>Boolean indicator.</returns>
    public bool SignOut()
    {
        return true;
    }

    /// <summary>
    /// Validate user token.
    /// </summary>
    /// <returns>Boolean indicator.</returns>
    public bool ValidateToken()
    {
        return true;
    }
}