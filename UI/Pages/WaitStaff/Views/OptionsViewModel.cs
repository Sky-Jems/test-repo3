using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reactive;
using System.Reactive.Linq;
using System.Threading.Tasks;
using System.Windows.Input;
using Avalonia.Controls;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Extensions;
using Pos.Models;
using ReactiveUI;

namespace Pos.Pages.WaitStaff;

public partial class OptionsViewModel : ReactiveObject, IRoutableViewModel
{
    public int selectedVariantId = 0;
    public event EventHandler<EventArgs> LoadOptionGroup;
    public event EventHandler<EventArgs> LoadOptionGroupValues;
    private readonly IProductService _productService;
    public string? UrlPathSegment => throw new System.NotImplementedException();
    public IScreen HostScreen { get; }
    public Product MenuItem { get; set; }
    public ObservableCollection<OptionItem> Options { get; set; } = new();
    public ObservableCollection<OptionGroup> OptionGroups { get; set; } = new();
    public ObservableCollection<TabItem> Tabs { get; set; } = new();
    public ObservableCollection<Variant> Variants { get; set; } = new();
    public ReactiveCommand<Unit, Unit> LoadProductOptionListCommand { get; }
    public ReactiveCommand<Unit, Unit> LoadProductOptionValueListCommand { get; }
    public ReactiveCommand<Unit, Unit> LoadProductVariantListCommand { get; }
    public ICommand ClickVariantCommand { get; }
    public ReactiveCommand<Unit, Unit> ClickPlusCommand { get; }
    public ReactiveCommand<Unit, Unit> ClickMinusCommand { get; }
    private readonly IOrderService _orderService;
    private readonly ICartService _cartService;
    private int _selectedItemQuantity;
    public int SelectedItemQuantity
    {
        get => _selectedItemQuantity;
        set
        {
            this.RaiseAndSetIfChanged(ref _selectedItemQuantity, value);
            this.RaisePropertyChanged(nameof(SelectedItemQuantityDisplay));
        }
    }
    public string SelectedItemQuantityDisplay => $"( {SelectedItemQuantity} )";


    public OptionsViewModel(IScreen screen, Product menuItem, IProductService productService)
    {
        this.HostScreen = screen;
        this.MenuItem = menuItem;
        _productService = productService;
        _orderService = ServiceLocator.Services.GetRequiredService<IOrderService>();
        _cartService = ServiceLocator.Services.GetRequiredService<ICartService>();
        LoadProductOptionListCommand = ReactiveCommand.CreateFromTask(LoadProductOptions);
        LoadProductOptionValueListCommand = ReactiveCommand.CreateFromTask(LoadProductOptionValues);
        LoadProductVariantListCommand = ReactiveCommand.CreateFromTask(LoadProductVariants);
        ClickVariantCommand = ReactiveCommand.CreateFromTask<Variant>(HandleClickVariantAsync);
        ClickPlusCommand = ReactiveCommand.CreateFromTask(HandleClickPlusAsync);
        ClickMinusCommand = ReactiveCommand.CreateFromTask(HandleClickMinusAsync);

        _cartService.WhenAnyValue(x => x.SelectedItem)
            .ObserveOn(RxApp.MainThreadScheduler)
            .Subscribe(item =>
            {
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

    public void ReloadOptionGroup()
    {
        LoadProductOptionListCommand.Execute().Subscribe();
    }

    public void ReloadOptionValues()
    {
        LoadProductOptionValueListCommand.Execute().Subscribe();
    }

    public void ReloadVariants()
    {
        LoadProductVariantListCommand.Execute().Subscribe();
    }

    private async Task LoadProductOptions()
    {
        try
        {
            if (MenuItem.Id != null)
            {
                var optionList = await _productService.GetProductOptionsAsync((int)MenuItem.Id);
                OptionGroups.Clear();
                foreach (var option in optionList)
                {
                    if (selectedVariantId is 0) selectedVariantId = (int)option.Id;
                    OptionGroups.Add(new OptionGroup
                    {
                        Id = option.Id,
                        Name = option.Name,
                        ProductId = option.ProductId,
                        Values = new ObservableCollection<OptionItem>(option.Values)
                    });
                }
            }
            LoadOptionGroup?.Invoke(this, EventArgs.Empty);
        }
        catch (Exception e)
        {
            Console.WriteLine("Option Group Load Failed...");
        }
    }

    private async Task LoadProductOptionValues()
    {
        try
        {
            var optionValueList = await _productService.GetProductOptionValuesAsync(selectedVariantId);
            Options.Clear();
            foreach (var option in optionValueList)
            {
                Options.Add(option);
            }
            LoadOptionGroupValues?.Invoke(this, EventArgs.Empty);
        }
        catch (Exception e)
        {
            Console.WriteLine("Option Load Failed...");
        }
    }

    private async Task LoadProductVariants()
    {
        try
        {
            var variants = await _productService.GetVariantsByProductAndOption((int)MenuItem.Id, selectedVariantId);
            Variants.Clear();
            foreach (var variant in variants)
            {
                Variants.Add(variant);
            }
            LoadOptionGroupValues?.Invoke(this, EventArgs.Empty);
        }
        catch (Exception e)
        {
            Console.WriteLine("Variant Load Failed...");
        }
    }
    public ReactiveCommand<Unit, IRoutableViewModel> GoBack => this.HostScreen.Router.NavigateBack;

    private async Task HandleClickVariantAsync(Variant variant)
    {
        var item = new LineItem
        {
            ProductId = variant.VariantId,
            Sku = variant.Sku,
            Quantity = 1,
            Price = (decimal)variant.Price
        };

        var addedItem = _cartService.AddItem(item, incrementIfExists: false);
        _cartService.SelectedItem = addedItem;

        long orderId;

        if (_cartService.OrderId is null)
        {
            var newOrder = new Order
            {
                Customer = _cartService.CustomerName,
                TableNumber = 1,
                LineItems = []
            };

            var createdOrder = await _orderService.AddOrder(newOrder);
            orderId = createdOrder.OrderId;
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

    private async Task HandleClickPlusAsync()
    {
        var selectedItem = _cartService.SelectedItem;
        if (selectedItem is null)
            return;

        _cartService.AddItem(selectedItem);

        if (_cartService.OrderId is null)
            return;

        // var updatedOrder = await _orderService.UpdateOrder(_cartService.GetSelectedOrder());
        // _cartService.LoadOrder(updatedOrder);
    }

    private async Task HandleClickMinusAsync()
    {
        var selectedItem = _cartService.SelectedItem;
        if (selectedItem is null)
            return;

        var previousQuantity = selectedItem.Quantity;
        _cartService.RemoveItem(selectedItem);

        if (_cartService.OrderId is null)
            return;

        var orderId = _cartService.OrderId.Value;

        // var updatedOrder = previousQuantity == 1
        //     ? await _orderService.RemoveItem(orderId, selectedItem.ProductId)
            // : await _orderService.UpdateOrder(_cartService.GetSelectedOrder());

        // _cartService.LoadOrder(updatedOrder);
    }
}
