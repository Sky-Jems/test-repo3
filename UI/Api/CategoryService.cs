using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using pos.Handlers;
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
        Console.WriteLine("Getting all categories...");
        var response = await _httpClient.GetJsonAsync<List<Category>>("categories");
        return response ?? new List<Category>();
    }
}
