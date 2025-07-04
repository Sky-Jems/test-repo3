using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using pos.Handlers.Interfaces;
using Pos.Models;

namespace pos.Api
{
    public class AuthService : IAuthService
    {
        private readonly TokenStore _tokenStore;
        private readonly IHttpHandler _client;

        public AuthService(TokenStore tokenStore, IHttpHandler httpHandler)
        {
            _tokenStore = tokenStore;
            _client = httpHandler;
        }

        public async Task<string> LoginAsync(string username, string password)
        {
            var response = await _client.PostJsonAsync("auth/login", new { username, password }, this);

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                throw new Exception($"Login failed: {errorContent}");
            }

            var loginResult = await response.Content.ReadFromJsonAsync<AuthResponseDto>();

            if (string.IsNullOrWhiteSpace(loginResult?.AccessToken) || string.IsNullOrWhiteSpace(loginResult?.RefreshToken))
                throw new Exception("Access or Refresh token missing in login response.");

            _tokenStore.AccessToken = loginResult.AccessToken;
            _tokenStore.RefreshToken = loginResult.RefreshToken;

            return loginResult.AccessToken;
        }

        public async Task<string> StaffLoginAsync(int userId)
        {
            var response = await _client.PostJsonAsync("auth/login", new { userId }, this);

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                throw new Exception($"Login failed: {errorContent}");
            }

            var loginResult = await response.Content.ReadFromJsonAsync<AuthResponseDto>();

            if (string.IsNullOrWhiteSpace(loginResult?.AccessToken) || string.IsNullOrWhiteSpace(loginResult?.RefreshToken))
                throw new Exception("Access or Refresh token missing in login response.");

            _tokenStore.AccessToken = loginResult.AccessToken;
            _tokenStore.RefreshToken = loginResult.RefreshToken;

            return loginResult.AccessToken;
        }

        public async Task<string> GetAccessTokenAsync()
        {
            if (_tokenStore.IsAccessTokenExpired())
            {
                return await RefreshTokenAsync();
            }

            return _tokenStore.AccessToken ?? throw new InvalidOperationException("Access token is missing.");
        }

        public async Task<string> RefreshTokenAsync()
        {
            if (string.IsNullOrWhiteSpace(_tokenStore.RefreshToken))
                throw new Exception("No refresh token available");

            var response = await _client.PostJsonAsync("auth/refresh", new { refreshToken = _tokenStore.RefreshToken }, this);

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                throw new Exception($"Token refresh failed: {errorContent}");
            }

            var tokenResult = await response.Content.ReadFromJsonAsync<AuthResponseDto>();

            if (string.IsNullOrWhiteSpace(tokenResult?.AccessToken))
                throw new Exception("Token refresh response is missing access token.");

            _tokenStore.AccessToken = tokenResult.AccessToken;

            return tokenResult.AccessToken;
        }

        public async Task LogoutAsync()
        {
            if (string.IsNullOrWhiteSpace(_tokenStore.RefreshToken))
                throw new Exception("No refresh token available to logout.");

            var response = await _client.PostJsonAsync("auth/logout", new { refreshToken = _tokenStore.RefreshToken }, this);

            if (!response.IsSuccessStatusCode)
            {
                var error = await response.Content.ReadAsStringAsync();
                throw new HttpRequestException($"Logout failed ({(int)response.StatusCode}): {error}");
            }

            _tokenStore.AccessToken = null;
            _tokenStore.RefreshToken = null;
        }

        public async Task<List<Staff>> GetStaffAsync()
        {
            var staffList = await _client.GetJsonAsync<List<Staff>>("user/get-all-users", this);
            return staffList ?? new List<Staff>();
        }
    }
}