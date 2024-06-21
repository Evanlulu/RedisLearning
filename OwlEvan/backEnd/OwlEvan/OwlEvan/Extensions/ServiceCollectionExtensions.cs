using OwlEvan.Repositories;
using OwlEvan.Repositories.Interfaces;
using OwlEvan.Services;

namespace OwlEvan.Extensions;

public static class ServiceCollectionExtensions
{
    public static void AddApplicationServices(this IServiceCollection services)
    {
        // Register repositories
        services.AddScoped<IproductsRepository, ProductsRepository>();
        // services.AddScoped<IUserRepository, UserRepository>();

        // Register services
        services.AddScoped<ProductsService>();
        // services.AddScoped<UserService>();

    }
}