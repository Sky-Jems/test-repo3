using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;

namespace pos.Handlers.Interfaces;

public interface IHttpHandler
{
    Task<TValue> GetJsonAsync<TValue>(string endpoint, object? caller = null);
    Task<HttpResponseMessage> PostJsonAsync(string endpoint, object postData, object? caller = null);
    Task<HttpResponseMessage> PutJsonAsync(string endpoint, object putData, object? caller = null);
    Task<HttpResponseMessage> DeleteAsync(string endpoint, object? caller = null);
    Task<T> ReadJsonResponseAsync<T>(HttpResponseMessage response);
}