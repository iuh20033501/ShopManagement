package fit.iuh.edu.vn.productservice.controller;

import fit.iuh.edu.vn.productservice.entity.Product;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fit.iuh.edu.vn.productservice.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    private static final String SERVICE_STAFF ="serviceStaff";

//Rate Limter Client
    @RateLimiter(name = SERVICE_STAFF)
    @GetMapping("/getAll")
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @GetMapping("/{productId}/price")
    public Double getProductPrice(@PathVariable String productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return product.getPrice();
        } else {
            return null;
        }
    }
    @GetMapping("/{productId}/quantity")
    public Integer getProductQuantity(@PathVariable String productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return product.getQuantity();
        } else {
            return null;
        }
    }

    @PostMapping("/reduce/{productId}")
    public ResponseEntity<?> reduceProductQuantity(@PathVariable String productId) {
        if (productId == null || productId.isEmpty()) {
            return ResponseEntity.badRequest().body("Product ID cannot be empty");
        }
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Product not found");
        }
        Product product = productOptional.get();
        if (product.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Product is out of stock");
        }
        product.setQuantity(product.getQuantity() - 1);
        productRepository.save(product);

        return ResponseEntity.ok("Quantity of product " + productId + " reduced successfully");
    }
    @GetMapping("/findById/{id}")
    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }
    @PostMapping("/create")
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody Product product) {
        Product product1 = productRepository.findById(id).orElse(null);
        if (product1 != null) {
            product1.setName(product.getName());
            product1.setPrice(product.getPrice());
            return productRepository.save(product1);
        }
        return null;
    }

    @PutMapping("/delete/{id}")
    public Product deleteProduct(@PathVariable String id) {
        Product product1 = productRepository.findById(id).orElse(null);
        if(product1 != null) {
            productRepository.deleteById(id);
        }
        return null;
    }

}
