using Microsoft.AspNetCore.Mvc;
using OwlEvan.Services;

namespace OwlEvan.Controllers;

[Route("user")]
[ApiController]
public class UserController : ControllerBase
{
    private readonly UsersService _usersService;

    public UserController(UsersService usersService)
    {
        _usersService = usersService;
    }
    

}