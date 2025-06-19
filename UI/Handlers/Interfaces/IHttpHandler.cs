using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;

namespace pos.Handlers.Interfaces;

public interface IHttpHandler
{
    Task<TValue> GetJsonAsync<TValue>(string endpoint);
    Task<HttpResponseMessage> PostJsonAsync(string endpoint, object postData);
    Task<HttpResponseMessage> PutJsonAsync(string endpoint, object putData);
    Task<HttpResponseMessage> DeleteAsync(string endpoint);
    Task<T> ReadJsonResponseAsync<T>(HttpResponseMessage response);
}