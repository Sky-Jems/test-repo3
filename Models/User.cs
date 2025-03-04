namespace POS.Models;

enum UserRole
{
    ADMIN,
    EMPLOYEE

}
public class User
{
    public Guid UserId { get; set; }
    public string Username { get; set; }
    public string Password { get; set; }
    public UserRole Role { get; set; }
    public string CreatedAt { get; set; }
    public string UpdatedAt { get; set; }
}
