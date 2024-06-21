using Microsoft.EntityFrameworkCore;
using OwlEvan.Data;
using OwlEvan.Models;
using OwlEvan.Repositories.Interfaces;

namespace OwlEvan.Repositories;

public class UsersRepository : EntityFrameworkRepository<User>, IUsersRepository 
{
    public UsersRepository(AppDbContext context) : base(context)
    {
    }
}