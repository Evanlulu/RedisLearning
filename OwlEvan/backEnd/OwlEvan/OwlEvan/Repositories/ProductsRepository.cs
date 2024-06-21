using Microsoft.EntityFrameworkCore;
using OwlEvan.Data;
using OwlEvan.Models;
using OwlEvan.Repositories.Interfaces;

namespace OwlEvan.Repositories;

public class ProductsRepository : EntityFrameworkRepository<Product>, IproductsRepository 
{
    public ProductsRepository(AppDbContext context) : base(context)
    {
    }
}