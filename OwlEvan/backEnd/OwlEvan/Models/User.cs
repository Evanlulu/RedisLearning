using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace OwlEvan.Models;

public class User
{
    public int Id { get; set; }
    
    [Required]
    public string Name { get; set; }
    
    [Required]
    public string Password { get; set; }
    
    [Required]
    public long LastLoginTime  { get; set; }
}