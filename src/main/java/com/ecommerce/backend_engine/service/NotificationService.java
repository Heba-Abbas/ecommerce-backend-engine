package com.ecommerce.backend_engine.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    //  قبل التحسين
    public void sendNotificationSync(Long orderId) {
        log.info(">>> [SYNC] START Sending Notification & Invoice for Order: {} | Thread: {}", orderId, Thread.currentThread().getName());
        try {

            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("<<< [SYNC] FINISH Sending Notification for Order: {}", orderId);
    }

    //  بعد التحسين
    @Async("asyncExecutor")
    public void sendNotificationAsync(Long orderId) {
        log.info(">>> [ASYNC] START Sending Notification & Invoice for Order: {} | Thread: {}", orderId, Thread.currentThread().getName());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("<<< [ASYNC] FINISH Sending Notification for Order: {}", orderId);
    }
}