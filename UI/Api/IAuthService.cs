using System.Collections.Generic;
using System.Threading.Tasks;
using Pos.Models;

namespace pos.Api;
public interface IAuthService
{
    Task<string> LoginAsync(string username, string password);
    Task<string> GetAccessTokenAsync();
    Task<string> RefreshTokenAsync();
    Task LogoutAsync();
}