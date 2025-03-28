using POSLibrary.Shared.Enums;

namespace POSLibrary.Services.UserService.Domain.Entities;

public class User
{
    public int UserId { get; set; }
    public required string Username { get; set; }
    public required string Password { get; set; }
    public required UserRole Role { get; set; }
    public string? CreatedAt { get; set; }
    public string? UpdatedAt { get; set; }
}
