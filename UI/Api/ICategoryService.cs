using System.Collections.Generic;
using System.Threading.Tasks;
using Pos.Models;

namespace pos.Api;

public interface ICategoryService
{
    Task<List<Category>> GetAllCategoriesAsync();
    Task<GetCategoryResponseDto> CreateCategory(Category category);
    Task<GetCategoryResponseDto> UpdateCategory(long id, Category category);
    Task<GetCategoryResponseDto> DeleteCategory(long id);
    Task<Category> GetCategoryByIdAsync(int id);
}
