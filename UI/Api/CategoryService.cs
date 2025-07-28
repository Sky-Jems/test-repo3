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

    public async Task<GetCategoryResponseDto> CreateCategory(Category category)
    {
        var response = await _httpClient.PostJsonAsync("categories", category);
        return await _httpClient.ReadJsonResponseAsync<GetCategoryResponseDto>(response);
    }

    public async Task<GetCategoryResponseDto> UpdateCategory(long id, Category category)
    {
        var response = await _httpClient.PutJsonAsync($"categories/{id}", category);
        return await _httpClient.ReadJsonResponseAsync<GetCategoryResponseDto>(response);
    }

    public async Task<GetCategoryResponseDto> DeleteCategory(long id)
    {
        var response = await _httpClient.DeleteAsync($"categories/{id}");
        return await _httpClient.ReadJsonResponseAsync<GetCategoryResponseDto>(response);
    }

    public async Task<Category> GetCategoryByIdAsync(int id)
    {
        return await _httpClient.GetJsonAsync<Category>($"categories/{id}");
    }
}
