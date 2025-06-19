using System;
using System.IdentityModel.Tokens.Jwt;

namespace pos.Api
{
    public class TokenStore
    {
        public string? AccessToken { get; set; }
        public string? RefreshToken { get; set; }

        public bool IsAccessTokenExpired()
        {
            if (string.IsNullOrEmpty(AccessToken))
                return true;

            try
            {
                var handler = new JwtSecurityTokenHandler();
                var jwtToken = handler.ReadToken(AccessToken) as JwtSecurityToken;

                return jwtToken == null || jwtToken.ValidTo < DateTime.UtcNow.AddMinutes(1);
            }
            catch (Exception ex)
            {
                // Optionally log the error
                Console.WriteLine($"[TokenStore] Failed to parse token: {ex.Message}");
                return true; // Treat any failure as expired
            }
        }
    }
}