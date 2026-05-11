package com.ecommerce.backend_engine.service;

import com.ecommerce.backend_engine.entity.*;
import com.ecommerce.backend_engine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;

    // --- الطلب 1: حماية البيانات (النسخة غير الآمنة) ---
    @Transactional
    public void purchaseV1Unsafe(Long id, Integer qty) {
        Product p = productRepository.findById(id).orElseThrow();
        if (p.getStock() >= qty) {
            try { Thread.sleep(100); } catch (Exception e) {}
            p.setStock(p.getStock() - qty);
            productRepository.save(p);

            saveOrder(p, qty);
        }
    }

    // --- الطلب 1: حماية البيانات (النسخة الآمنة) ---
    @Transactional
    public void purchaseV1Safe(Long id, Integer qty) {
        Product p = productRepository.findByIdWithLock(id).orElseThrow();
        if (p.getStock() >= qty) {
            p.setStock(p.getStock() - qty);
            productRepository.save(p);

            saveOrder(p, qty);
        }
    }


    private void saveOrder(Product p, Integer qty) {
        ProductOrder order = new ProductOrder();
        order.setProduct(p);
        order.setQuantity(qty);
        order.setOrderDate(LocalDateTime.now());
        order.setIsProcessed(false);
        orderRepository.save(order);
    }

    // --- الطلب 2: إدارة الموارد (Thread Pool) ---
    public void taskV2Limitless() {
        new Thread(() -> {
            try { Thread.sleep(2000); } catch (Exception e) {}
        }).start();
    }

    @Async("taskExecutor")
    public void taskV2Controlled() {
        try { Thread.sleep(2000); } catch (Exception e) {}
    }
}