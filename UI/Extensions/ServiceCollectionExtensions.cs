using System;
using Microsoft.Extensions.DependencyInjection;
using pos.Api;
using pos.Handlers;
using pos.Handlers.Interfaces;

namespace pos.Extensions;

public static class ServiceCollectionExtensions
{
    public static void AddCommonServices(this IServiceCollection collection)
    {
        collection.AddSingleton<TokenStore>();
        collection.AddSingleton<IHttpHandler>(sp => new HttpHandler(sp));
        collection.AddSingleton<IAuthService, AuthService>();
        collection.AddSingleton<ICartService, CartService>();
        collection.AddSingleton<IProductService, ProductService>();
        collection.AddSingleton<ICategoryService, CategoryService>();
        collection.AddSingleton<IOrderService, OrderService>();
    }
}
