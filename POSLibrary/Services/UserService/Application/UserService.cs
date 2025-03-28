
using POSLibrary.Shared.Common.DTO;

namespace POSLibrary.Services.UserService.Application;

/// <summary>
/// Represents a service for managing a user.
/// </summary>
public class UserService(UserDto user)
{
    private UserDto _user = user;

    /// <summary>
    /// Get all list of users.
    /// </summary>
    /// <returns>A list of users.</returns>
    public List<UserDto> Get()
    {
        return [];
    }

    /// <summary>
    /// Get user by id.
    /// </summary>
    /// <param name="id">Id of the user.</param>
    /// <returns>An instance of user.</returns>
    public UserDto Get(int id)
    {
        return new UserDto
        {
            Username = ""
        };
    }

    /// <summary>
    /// Update user instance.
    /// </summary>
    /// <param name="user">user instance.</param>
    /// <returns>An instance of user.</returns>
    public UserDto Update(UserDto user)
    {
        return new UserDto
        {
            Username = ""
        };
    }

    /// <summary>
    /// Delete user instance.
    /// </summary>
    /// <param name="id">Id of the user.</param>
    /// <returns>A boolean.</returns>
    public bool Delete(int id)
    {
        return true;
    }
}