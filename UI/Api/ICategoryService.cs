using System.Collections.Generic;
using System.Threading.Tasks;
using Pos.Models;

namespace pos.Api;

public interface ICategoryService
{
    Task<List<Category>> GetAllCategoriesAsync();
    Task CreateCategory(Category category);
    Task UpdateCategory(long id, Category category);
    Task DeleteCategory(long id);
    Task<Category> GetCategoryByIdAsync(int id);
}
