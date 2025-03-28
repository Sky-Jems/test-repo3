using POSLibrary.Shared.Enums;

namespace POSLibrary.Shared.Common.DTO;

public class UserDto
{
    public int UserId { get; set; }
    public required string Username { get; set; }
    public UserRole Role { get; set; }
}
