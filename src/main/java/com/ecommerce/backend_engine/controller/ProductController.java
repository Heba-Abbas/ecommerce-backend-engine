package com.ecommerce.backend_engine.controller;

import com.ecommerce.backend_engine.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private BatchService batchService;
    @Autowired private NotificationService notificationService; // إضافة حقن خدمة الإشعارات

    // --- الطلب 1: حماية البيانات (Pessimistic Locking) ---
    @PostMapping("/v1/unsafe")
    public String v1u(@RequestParam Long id) { productService.purchaseV1Unsafe(id, 1); return "Done Unsafe"; }

    @PostMapping("/v1/safe")
    public String v1s(@RequestParam Long id) { productService.purchaseV1Safe(id, 1); return "Done Safe"; }

    // --- الطلب 2: إدارة الموارد (Thread Pool) ---
    @PostMapping("/v2/limitless")
    public String v2l() { productService.taskV2Limitless(); return "Started Limitless Threads"; }

    @PostMapping("/v2/controlled")
    public String v2c() { productService.taskV2Controlled(); return "Started Controlled Threads (Pool)"; }

    // --- الطلب 3: المعالجة غير المتزامنة (Async) ---
    // تعديل: استدعاء NotificationService مباشرة لتفعيل الـ Logs و الـ @Async
    @PostMapping("/v3/sync")
    public String v3s(@RequestParam Long id) {
        notificationService.sendNotificationSync(id);
        return "Sync Notification Done (User Waited 1.5s)";
    }

    @PostMapping("/v3/async")
    public String v3a(@RequestParam Long id) {
        notificationService.sendNotificationAsync(id);
        return "Async Notification Started (User Received Response Immediately)";
    }

    // --- الطلب 4: معالجة الدفعات (Batch Processing) ---
    @GetMapping("/v4/heavy")
    public String v4h() { return "Processed Heavy: " + batchService.processV4Heavy(); }

    @GetMapping("/v4/chunks")
    public String v4c() { return "Processed Chunks: " + batchService.processV4Chunks(); }
}