using System.Collections.Generic;
using System.Threading.Tasks;
using Pos.Models;

namespace pos.Api;

public interface IProductService
{
    Task<List<OptionGroupDto>> GetProductOptionsAsync(long productId);
    Task<List<OptionItem>> GetProductOptionValuesAsync(int variantId);
    Task<List<Product>> GetProductsByCategoryAsync(Category subCategory);
    Task<List<Product>> GetAllProducts();
    Task<int> AddProduct(ProductDto product);
    Task DeleteProductByIdAsync(int id);
    Task UpdateProductAsync(ProductDto product);
    Task<List<Variant>> GetVariantsByProductAndOption(long productId, long? optionId = null, string? optionValue = null);
    Task<Product> GetProductByIdAsync(int id);
}
