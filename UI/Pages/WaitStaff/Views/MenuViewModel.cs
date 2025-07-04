using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Reactive.Linq;
using System.Threading.Tasks;
using System.Windows.Input;
using AvaloniaDialogs.Views;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.WaitStaff;

public class MenuViewModel : ReactiveObject, IRoutableViewModel
{
    public string? UrlPathSegment => throw new System.NotImplementedException();
    public IScreen HostScreen { get; }
    private readonly IProductService _productService;
    private readonly ICartService _cartService;
    private readonly IOrderService _orderService;
    private Category Category { get; set; }
    public ObservableCollection<Product> Products { get; set; } = new();
    private ReactiveCommand<Unit, Unit> LoadProductsCommand { get; }
    public ICommand ProductCardClickedCommand { get; }
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
    private bool _isProcessingProduct = false;

    public MenuViewModel(IScreen screen, Category category)
    {
        HostScreen = screen;
        Category = category;

        _productService = ServiceLocator.Services.GetRequiredService<IProductService>();
        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        _orderService = ServiceLocator.Services.GetRequiredService<IOrderService>();

        LoadProductsCommand = ReactiveCommand.CreateFromTask(LoadProductsAsync);
        ProductCardClickedCommand = ReactiveCommand.CreateFromTask<Product>(HandleClickProduct);

        LoadProductsCommand.Execute().Subscribe();

        SubscribeToCartUpdates();
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
            var products = await _productService.GetProductsByCategoryAsync(Category);
            Products.Clear();
            foreach (var product in products)
            {
                Products.Add(product);
            }
            UpdateProductSelection(_cartService.SelectedItem);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw;
        }
    }

    private async Task HandleClickProduct(Product product)
    {
        if (_isProcessingProduct)
            return;

        if (_cartService.Items.Any(x => x.ProductId == product.Id))
            return;

        try
        {
            var item = new LineItem
            {
                ProductId = product.Id.Value,
                ProductName = product.Name,
                ProductDescription = product.Description,
                Category = Category,
                Quantity = 1,
                Price = product.Price
            };

            var addedItem = _cartService.AddItem(item, incrementIfExists: false);
            _cartService.SelectedItem = addedItem;

            long orderId;

            if (_cartService.OrderId is null)
            {
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
                .FirstOrDefault(x => x.ProductId == addedItem.ProductId);
        }
        finally
        {
            _isProcessingProduct = false;
        }
    }

    public ReactiveCommand<Unit, IRoutableViewModel> GoBack => HostScreen.Router.NavigateBack;
}
