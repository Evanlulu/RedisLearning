using OwlEvan.Repositories;
using OwlEvan.Repositories.Interfaces;
using OwlEvan.Services;

namespace OwlEvan.Extensions;

public static class ServiceCollectionExtensions
{
    public static void AddApplicationServices(this IServiceCollection services)
    {
        // Register repositories
        services.AddScoped<IProductsRepository, ProductsRepository>();
        services.AddScoped<IUsersRepository, UsersRepository>();

        // Register services
        services.AddScoped<ProductsService>();
        // services.AddScoped<UserService>();

    }
}