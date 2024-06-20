using Microsoft.EntityFrameworkCore;
using OwlEvan.Controllers;

namespace OwlEvan.Data;

public class AppDbContext  : DbContext
{
    public  AppDbContext(DbContextOptions<AppDbContext> options) : base(options){}
    
    public DbSet<Product> Products { get; set; }
}

