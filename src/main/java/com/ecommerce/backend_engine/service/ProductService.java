package com.ecommerce.backend_engine.service;

import com.ecommerce.backend_engine.entity.Product;
import com.ecommerce.backend_engine.entity.ProductOrder;
import com.ecommerce.backend_engine.repository.OrderRepository;
import com.ecommerce.backend_engine.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    //  عرض المنتجات
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //  إضافة منتج
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public String purchaseProduct(Long productId, Integer quantity) {

        Product product = productRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new RuntimeException("PRODUCT DOESN'T EXIST"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("THE INVENTORY IS NOT ENOUGH");
        }


        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        ProductOrder order = new ProductOrder();
        order.setProductId(productId);
        order.setQuantity(quantity);

        order.setOrderDate(LocalDateTime.now());
        order.setStatus("SUCCESS");

        orderRepository.save(order);

        return "SUCCESSFULLY DONE... .ORDER ID: " + order.getId() + System.currentTimeMillis();
    }
}