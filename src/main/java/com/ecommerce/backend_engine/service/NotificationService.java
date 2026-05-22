package com.ecommerce.backend_engine.service;
import com.ecommerce.backend_engine.event.OrderMessageEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    //قبل التحسين طلب3
    public void processFullOrderSync(Long orderId) {
        log.info(">>> [SYNC-BLOCKING] START Processing Order #{} | Thread: {}", orderId, Thread.currentThread().getName());
        try {
            log.info("1. Validate Payment (2.0s)...");
            Thread.sleep(2000);
            log.info("2. Update Inventory (1.0s)...");
            Thread.sleep(1000);
            log.info("3. Send Confirmation Email (3.0s)...");
            Thread.sleep(3000);
            log.info("4. Notify Warehouse (2.0s)...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("<<< [SYNC-BLOCKING] FINISH Processing Order #{}. Total Wait ~ 8.0s", orderId);
    }

    //بعد التحسين طلب3 باضافة  (Messaging Queue)

    @EventListener
    @Async("taskExecutor")
    public void handleOrderMessageFromQueue(OrderMessageEvent event) {
        Long orderId = event.getOrderId();
        log.info(">>> [QUEUE-CONSUMER] Popped Order #{} from Queue. Starting background tasks | Thread: {}", orderId, Thread.currentThread().getName());

        try {

            log.info("[QUEUE] Processing Payment & Inventory for Order #{}...", orderId);
            Thread.sleep(3000);
            log.info("[QUEUE] Sending Confirmation Email for Order #{}...", orderId);
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("<<< [QUEUE-CONSUMER] Successfully processed all background tasks for Order #{}", orderId);
    }
}