using POSLibrary.Services.InventoryService.Domain.Entities;
using POSLibrary.Services.OrderService.Domain.Entities;

namespace POSLibrary.Services.OrderService.Application.Interface;

public interface ICartService {
    void Add(int productId, int quantity, List<OptionGroup> optionGroup);
    void Update(int cartItemId, int quantity, List<OptionGroup> optionGroup);
    int? Remove(int cartItemId);
    float SubTotal();
    Order ToOrder();
    void Clear();
}