using System.Collections.Generic;
using System.Threading.Tasks;
using pos.Handlers.Interfaces;
using Pos.Models;

namespace pos.Api;

public class CategoryService : ICategoryService
{
    private readonly IHttpHandler _httpClient;

    public CategoryService(IHttpHandler httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<List<Category>> GetAllCategoriesAsync()
    {
        return await _httpClient.GetJsonAsync<List<Category>>("categories");
    }

    public async Task CreateCategory(Category category)
    {
        await _httpClient.PostJsonAsync("categories", category);
    }

    public async Task UpdateCategory(long id, Category category)
    {
        await _httpClient.PutJsonAsync($"categories/{id}", category);
    }

    public async Task DeleteCategory(long id)
    {
        await _httpClient.DeleteAsync($"categories/{id}");
    }
}
