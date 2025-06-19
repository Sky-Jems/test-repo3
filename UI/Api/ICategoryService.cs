using System.Collections.Generic;
using System.Threading.Tasks;
using Pos.Models;

namespace pos.Api;

public interface ICategoryService
{
    Task<List<Category>> GetAllCategoriesAsync();
}
