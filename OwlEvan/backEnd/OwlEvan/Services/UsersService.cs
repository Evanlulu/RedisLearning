using OwlEvan.Models;
using OwlEvan.Repositories.Interfaces;

namespace OwlEvan.Services;

public class UsersService
{
    private IUsersRepository _usersRepository;

    public UsersService(IUsersRepository usersRepository)
    {
        _usersRepository = usersRepository;
    }

    public async Task<IEnumerable<User>> GetAllUsersAsync()
    {
        return await _usersRepository.GetAllAsync();
    }
}