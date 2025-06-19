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

    public async Task AddVariantsAsync(List<OptionGroupDto> variants, long? productId = null)
    {
        if (productId != null && variants.Count == 0)
        {
            var payload = new
            {
                product_id = productId
            };
            await _httpClient.PostJsonAsync("variants", payload);
        }
        else
        {
            var payload = new
            {
                variant_options = variants
            };
            await _httpClient.PostJsonAsync("variant-options", payload);
        }
    }

    public async Task UpdateVariantAsync(OptionGroupDto optionValue)
    {
        await _httpClient.PutJsonAsync($"variant-options/{optionValue.Id}", optionValue);
    }

    public async Task<List<Product>> GetProductsByCategoryAsync(Category subCategory)
    {
        var response = await _httpClient.GetJsonAsync<List<Product>>($"categories/{subCategory.Id}/products");
        return response ?? new List<Product>();
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
        return response ?? new List<Product>();
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

    public async Task DeleteVariantByIdAsync(int id)
    {
        await _httpClient.DeleteAsync($"variant-options/{id}");
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
        return await _httpClient.GetJsonAsync<Product>($"products/{id}");
    }

    public async Task UpdateVariantCombinationAsync(long variantId, object variantPayload)
    {
        await _httpClient.PutJsonAsync($"variants/{variantId}", variantPayload);
    }

    public async Task<List<Combinations>> GetVariantCombinationListAsync(int id)
    {
        return await _httpClient.GetJsonAsync<List<Combinations>>($"product-variants/{id}");
    }
    public async Task<List<Combinations>> GetProductVariantsAsync(long productId, int valueIds)
    {
        return await _httpClient.GetJsonAsync<List<Combinations>>($"product-variants/{productId}?valueIds={valueIds}");
    }
}
