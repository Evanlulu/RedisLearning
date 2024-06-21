using Microsoft.AspNetCore.Mvc;
using OwlEvan.Models;
using OwlEvan.Repositories;
using OwlEvan.Repositories.Interfaces;

namespace OwlEvan.Services;

public class ProductsService
{
    private readonly IProductsRepository _productsRepository;

    public ProductsService(IProductsRepository productsRepository)
    {
        _productsRepository = productsRepository;
    }

    public async Task<IEnumerable<Product>> GetAllProductsAsync()
    {
        return await _productsRepository.GetAllAsync();
    }

    public async Task<Product> GetProductByIdAsync(int id)
    {
        return await _productsRepository.GetByIdAsync(id);
    }

    public async Task AddProductAsync(Product product)
    {
        await _productsRepository.AddAsync(product);
    }

    public async Task UpdateProductAsync(Product product)
    {
        await _productsRepository.UpdateAsync(product);
    }

    public async Task DeleteProductAsync(Product product)
    {
        await _productsRepository.DeleteAsync(product);
    }
}
