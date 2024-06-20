using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace OwlEvan.Controllers
{
    [Route("api/products")]
    [ApiController]
    public class ProductController : ControllerBase
    {
        private readonly List<Product> _products = new List<Product>()
        {
            new Product { Id = 1, Name = "Product 1", Price = 10.99m },
            new Product { Id = 2, Name = "Product 2", Price = 20.99m }
        };


        [HttpGet]
        public IActionResult GetProducts()
        {
            return Ok(_products);
        }


        [HttpGet("{id}")]
        public IActionResult GetProduct(int id)
        {
            var product = _products.Find(p => p.Id == id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }


        [HttpPost]
        public IActionResult CreateProduct(Product product)
        {
            _products.Add(product);
            return CreatedAtAction(nameof(GetProduct), new { id = product.Id }, product);
        }


        [HttpPut("{id}")]
        public IActionResult UpdateProduct(int id, Product product)
        {
            var existingProduct = _products.Find(p => p.Id == id);
            if (existingProduct == null)
            {
                return NotFound();
            }

            existingProduct.Name = product.Name;
            existingProduct.Price = product.Price;

            return NoContent();
        }
        
        [HttpDelete("{id}")]
        public IActionResult DeleteProduct(int id)
        {
            var existingProduct = _products.Find(p => p.Id == id);
            if (existingProduct == null)
            {
                return NotFound();
            }

            _products.Remove(existingProduct);

            return NoContent();
        }
    }

    public class Product
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public decimal Price { get; set; }
    }
}
