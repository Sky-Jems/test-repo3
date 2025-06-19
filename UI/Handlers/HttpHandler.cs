using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using pos.Handlers.Interfaces;
using pos.Api;
using Microsoft.Extensions.DependencyInjection;

namespace pos.Handlers
{
    public class HttpHandler : IHttpHandler
    {
        private readonly HttpClient _client;
        private readonly IServiceProvider _provider;

        public HttpHandler(IServiceProvider provider)
        {
            _provider = provider;
            _client = new HttpClient
            {
                // base.DefaultRequestHeaders.Add("fad", "fad");
                BaseAddress = new Uri("http://localhost:8081/")
            };
        }

        private async Task AddAuthHeaderAsync()
        {
            _client.DefaultRequestHeaders.Authorization = null;

            try
            {
                var authService = _provider.GetRequiredService<IAuthService>();
                var accessToken = await authService.GetAccessTokenAsync();

                if (!string.IsNullOrWhiteSpace(accessToken))
                {
                    _client.DefaultRequestHeaders.Authorization =
                        new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", accessToken);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"[HttpHandler] Failed to get access token: {ex.Message}");
            }
        }

        public async Task<TValue> GetJsonAsync<TValue>(string endpoint)
        {
            await AddAuthHeaderAsync();
            return await _client.GetFromJsonAsync<TValue>(endpoint);
        }

        public async Task<HttpResponseMessage> PostJsonAsync(string endpoint, object postData)
        {
            await AddAuthHeaderAsync();
            return await _client.PostAsJsonAsync(endpoint, postData);
        }

        public async Task<HttpResponseMessage> PutJsonAsync(string endpoint, object putData)
        {
            await AddAuthHeaderAsync();
            return await _client.PutAsJsonAsync(endpoint, putData);
        }

        public async Task<HttpResponseMessage> DeleteAsync(string endpoint)
        {
            await AddAuthHeaderAsync();
            return await _client.DeleteAsync(endpoint);
        }

        public async Task<T> ReadJsonResponseAsync<T>(HttpResponseMessage response)
        {
            response.EnsureSuccessStatusCode();
            using var stream = await response.Content.ReadAsStreamAsync();
            return await JsonSerializer.DeserializeAsync<T>(stream);
        }
    }
}
