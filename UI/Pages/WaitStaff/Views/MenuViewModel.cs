using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Reactive.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Windows.Input;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using Pos.Dialogs;
using Pos.Models;
using Pos.Util;
using ReactiveUI;

namespace Pos.Pages.WaitStaff;

public class MenuViewModel : ReactiveObject, IRoutableViewModel
{
    public string? UrlPathSegment => throw new System.NotImplementedException();
    public IScreen HostScreen { get; }
    private readonly IProductService _productService;
    private readonly ICartService _cartService;
    private readonly IOrderService _orderService;
    private readonly IOrderTransactionService _orderTransactionService;
    private Category Category { get; set; }
    public ObservableCollection<Product> Products { get; set; } = new();
    private ReactiveCommand<Unit, Unit> LoadProductsCommand { get; }
    public ReactiveCommand<Product, Unit> ProductCardClickedCommand { get; }
    private int _selectedItemQuantity;
    private int SelectedItemQuantity
    {
        get => _selectedItemQuantity;
        set
        {
            this.RaiseAndSetIfChanged(ref _selectedItemQuantity, value);
            this.RaisePropertyChanged(nameof(SelectedItemQuantityDisplay));
        }
    }
    public string SelectedItemQuantityDisplay => $"( {SelectedItemQuantity} )";
    private bool _isProcessingProduct;

    private List<Product> _products = new();


    private string _searchText = "";
    public string SearchText
    {
        get => _searchText;
        set
        {
            this.RaiseAndSetIfChanged(ref _searchText, value);
            FilterProducts();
        }
    }

    public MenuViewModel(IScreen screen, Category category)
    {
        HostScreen = screen;
        Category = category;

        _productService = ServiceLocator.Services.GetRequiredService<IProductService>();
        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        _orderService = ServiceLocator.Services.GetRequiredService<IOrderService>();
        _orderTransactionService = ServiceLocator.Services.GetRequiredService<IOrderTransactionService>();

        LoadProductsCommand = ReactiveCommand.CreateFromTask(LoadProductsAsync);
        ProductCardClickedCommand = ReactiveCommand.CreateFromTask<Product>(HandleClickProduct);

        LoadProductsCommand.Execute().Subscribe();

        SubscribeToCartUpdates();
        
        LoadProductsCommand.ThrownExceptions
            .Subscribe(_ => HandleCommandError("Error.", "Failed to load products."));
        ProductCardClickedCommand.ThrownExceptions
            .Subscribe(_ => HandleCommandError("Error.", "Failed to add item."));
    }

    private void SubscribeToCartUpdates()
    {
        _cartService.WhenAnyValue(x => x.SelectedItem)
            .ObserveOn(RxApp.MainThreadScheduler)
            .Subscribe(item =>
            {
                UpdateProductSelection(item);
                if (item is null)
                {
                    SelectedItemQuantity = 0; // reset on clear
                }
                else
                {
                    item.WhenAnyValue(i => i.Quantity)
                        .StartWith(item.Quantity)
                        .ObserveOn(RxApp.MainThreadScheduler)
                        .Subscribe(qty => SelectedItemQuantity = qty);
                }
            });
    }

    public void UpdateCategory(Category newCategory)
    {
        if (!Category.Equals(newCategory))
        {
            Category = newCategory;
            LoadProductsCommand.Execute().Subscribe();
        }
    }

    private void UpdateProductSelection(LineItem? selectedItem)
    {
        foreach (var product in Products)
        {
            product.IsSelected = selectedItem?.ProductId == product.Id;
        }
    }

    private async Task LoadProductsAsync()
    {
        try
        {
            _products = await _productService.GetProductsByCategoryAsync(Category);
            Products.Clear();
            foreach (var product in _products)
            {
                Products.Add(product);
            }
            FilterProducts();
            UpdateProductSelection(_cartService.SelectedItem);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
    }
    
    private async Task<bool> ValidateCustomerNameAsync()
    {
        var startDate = new DateTimeOffset(DateTime.UtcNow).DayStart();
        var endDate = new DateTimeOffset(DateTime.UtcNow).DayEnd();
        string start = HttpUtility.UrlEncode(startDate.ToISO8601());
        string end = HttpUtility.UrlEncode(endDate.ToISO8601());
        var pendingOrders = await _orderTransactionService
            .GetFilteredOrderTransactionsByStatus(Constants.OrderStatusType.PENDING.ToString(), start, end);
        if (string.IsNullOrWhiteSpace(_cartService.Customer))
            return false;

        // Check if any order has the same customer name (case-insensitive)
        var existingCustomer = pendingOrders
            .Any(order =>
                order.OrderId != _cartService.OrderId && // Exclude current order
                string.Equals(order.Order.Customer?.Trim(), _cartService.Customer.Trim(), StringComparison.OrdinalIgnoreCase)
            );

        return existingCustomer;
    }

    private async Task HandleClickProduct(Product product)
    {
        if (!_cartService.CanModifyItems)
        {
            await new InfoDialog
            {
                Title = "Can't modify item",
                Message = "Items cannot be modified because the order is already completed or partially paid.",
                ButtonText = "OK"
            }.ShowAsync();

            return;
        }

        if (string.IsNullOrWhiteSpace(_cartService.Customer))
        {
            await new InfoDialog
            {
                Title = "Can't add item",
                Message = "Please enter a customer name before adding items.",
                ButtonText = "OK"
            }.ShowAsync();

            var uiInteractionService = ServiceLocator.Services.GetRequiredService<IUIInteractionService>();
            uiInteractionService.FocusCustomerField();

            return;
        }

        if (_isProcessingProduct || product.Id is null)
            return;

        var existingItem = _cartService.Items.FirstOrDefault(x => x.ProductId == product.Id);
        if (existingItem is not null)
        {
            _cartService.SelectedItem = existingItem;
            return;
        }

        try
        {
            _isProcessingProduct = true;
            var item = new LineItem
            {
                ProductId = product.Id.Value,
                ProductName = product.Name,
                ProductDescription = product.Description,
                Category = Category,
                Quantity = 1,
                Price = product.Price
            };

            _cartService.SelectedItem = item;

            long orderId;

            if (_cartService.OrderId is null)
            {
                var isExistingCustomer = await ValidateCustomerNameAsync();
                if (isExistingCustomer)
                {
                    await new InfoDialog
                    {
                        Title = "Can't add item",
                        Message = $"A pending order already exists for '{_cartService.Customer}'. Please enter a different name.",
                        ButtonText = "OK"
                    }.ShowAsync();

                    return;
                }

                var newOrder = new Order
                {
                    Customer = _cartService.Customer,
                    TableNumber = 1,
                    LineItems = []
                };

                var createdOrder = await _orderService.AddOrder(newOrder);
                orderId = createdOrder.OrderId;
                _cartService.OrderId = createdOrder.OrderId;
            }
            else
            {
                orderId = _cartService.OrderId.Value;
            }

            var lineItemDto = LineItemMapper.ToDto(item, orderId);
            var updatedOrder = await _orderService.AddLineItem(lineItemDto);

            _cartService.LoadOrder(updatedOrder);
            _cartService.SelectedItem = _cartService.Items
                .FirstOrDefault(x => x.ProductId == item.ProductId);
        }
        finally
        {
            _isProcessingProduct = false;
        }
    }

    private void FilterProducts()
    {
        _products = _products.Distinct().ToList();
        Products.Clear();
        IEnumerable<Product> filtered;
        if (string.IsNullOrWhiteSpace(SearchText))
        {
            filtered = _products;
        }
        else
        {
            filtered = _products.Where(p => p.Name?.Contains(SearchText, StringComparison.OrdinalIgnoreCase) == true);
        }

        foreach (var product in filtered)
            Products.Add(product);
    }

    public ReactiveCommand<Unit, IRoutableViewModel> GoBack => HostScreen.Router.NavigateBack;
    
    private async void HandleCommandError(string title, string message)
    {
        await new InfoDialog
        {
            Title = title,
            Message = message,
            ButtonText = "OK"
        }.ShowAsync();
    }
}
