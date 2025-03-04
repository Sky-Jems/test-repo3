using POS.Models;

namespace POS.Services;

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
    public User SignIn(string username, string password)
    {

    }

    /// <summary>
    /// Register new user.
    /// </summary>
    /// <param name="username">username of the current user.</param>
    /// <param name="password">password of the current user.</param>
    /// <param name="role">role of the current user.</param>
    /// <returns>An instance newly registered user.</returns>
    public User SignUp(string username, string password, UserRole role)
    {

    }

    /// <summary>
    /// Signout user.
    /// </summary>
    /// <returns>Boolean indicator.</returns>
    public bool SignOut()
    {

    }

    /// <summary>
    /// Validate user token.
    /// </summary>
    /// <returns>Boolean indicator.</returns>
    public bool ValidateToken()
    {

    }
}