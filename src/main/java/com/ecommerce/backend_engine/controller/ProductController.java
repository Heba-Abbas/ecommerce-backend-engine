package com.ecommerce.backend_engine.controller;

import com.ecommerce.backend_engine.entity.Product;
import com.ecommerce.backend_engine.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> list() {
        return productService.getAllProducts();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PostMapping("/{id}/purchase")
    public String buy(@PathVariable Long id, @RequestParam Integer quantity) {
        return productService.purchaseProduct(id, quantity);
    }
}