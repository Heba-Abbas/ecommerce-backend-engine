package com.ecommerce.backend_engine.service;

import com.ecommerce.backend_engine.entity.*;
import com.ecommerce.backend_engine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;

    // --- الطلب 1: حماية البيانات (Race Condition) ---
    @Transactional
    public void purchaseV1Unsafe(Long id, Integer qty) {
        Product p = productRepository.findById(id).get();
        if (p.getStock() >= qty) {
            try { Thread.sleep(100); } catch (Exception e) {}
            p.setStock(p.getStock() - qty);
            productRepository.save(p);
        }
    }

    @Transactional
    public void purchaseV1Safe(Long id, Integer qty) {
        Product p = productRepository.findByIdWithLock(id).get(); // استخدام القفل التشاؤمي
        if (p.getStock() >= qty) {
            p.setStock(p.getStock() - qty);
            productRepository.save(p);
        }
    }

    // --- الطلب 2: إدارة الموارد (Thread Pool) ---
    public void taskV2Limitless() {
        // قبل التحسين: إنشاء خيوط جديدة بلا قيود
        new Thread(() -> {
            try { Thread.sleep(2000); } catch (Exception e) {}
        }).start();
    }

    @Async("taskExecutor") // بعد التحسين
    public void taskV2Controlled() {
        try { Thread.sleep(2000); } catch (Exception e) {}
    }

    // --- الطلب 3: المعالجة غير المتزامنة (Async) ---
    public void notifyV3Sync() {
        try { Thread.sleep(1500); } catch (Exception e) {}
    }

    @Async("taskExecutor")
    public void notifyV3Async() {
        try { Thread.sleep(1500); } catch (Exception e) {}
    }
}