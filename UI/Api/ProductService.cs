using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using pos.Handlers;
using pos.Handlers.Interfaces;
using Pos.Models;

namespace pos.Api;

public class ProductService : IProductService
{
    private readonly IHttpHandler _httpClient;

    public ProductService(IHttpHandler httpClient)
    {
       _httpClient = httpClient;
    }

    public async Task<List<Product>> GetProductsByCategoryAsync(Category category)
    {
        return await _httpClient.GetJsonAsync<List<Product>>($"categories/{category.Id}/products");
    }

    public async Task<List<OptionGroupDto>> GetProductOptionsAsync(long productId)
    {
        var response = await _httpClient.GetJsonAsync<List<OptionGroupDto>>($"products/{productId}/variant-options");
        return response;
    }

    public async Task<List<OptionItem>> GetProductOptionValuesAsync(int variantId)
    {
        var response = await _httpClient.GetJsonAsync<List<OptionItem>>($"variant-options/{variantId}/variant-option-values");
        return response;

    }

    public async Task<List<Product>> GetAllProducts()
    {
        var response = await _httpClient.GetJsonAsync<List<Product>>("products");
        var products = response ?? new List<Product>();

        foreach (var product in products)
        {
            product.Price = (double)99.99m;
        }

        return products;

    }

    public async Task<int> AddProduct(ProductDto product)
    {
        var response = await _httpClient.PostJsonAsync("products", product);
        response.EnsureSuccessStatusCode();

        var stream = await response.Content.ReadAsStreamAsync();
        var createdProduct = await JsonSerializer.DeserializeAsync<Product>(stream, new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true
        });

        return (int)createdProduct.Id;
    }

    public async Task DeleteProductByIdAsync(int id)
    {
        await _httpClient.DeleteAsync($"products/{id}");
    }

    public async Task UpdateProductAsync(ProductDto product)
    {
        await _httpClient.PutJsonAsync($"products/{product.Id}", product);
    }

    public async Task<List<Variant>> GetVariantsByProductAndOption(long productId, long? optionId = null, string? optionValue = null)
    {
        var queryParams = new List<string>();
        if (optionId.HasValue)
            queryParams.Add($"variantOptionId={optionId.Value}");

        if (!string.IsNullOrWhiteSpace(optionValue))
            queryParams.Add($"valueIds={Uri.EscapeDataString(optionValue)}");

        var queryString = queryParams.Any() ? "?" + string.Join("&", queryParams) : "";

        var url = $"product-variants/{productId}{queryString}";

        return await _httpClient.GetJsonAsync<List<Variant>>(url);
    }

    public async Task<Product> GetProductByIdAsync(int id)
    {
        var product = await _httpClient.GetJsonAsync<Product>($"products/{id}");
        if (product != null)
        {
            product.Price = (double)22.99m;
        }

        return product;
    }

}
