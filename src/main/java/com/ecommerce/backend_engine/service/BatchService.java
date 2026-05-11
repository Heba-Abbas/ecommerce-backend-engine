package com.ecommerce.backend_engine.service;

import com.ecommerce.backend_engine.entity.ProductOrder;
import com.ecommerce.backend_engine.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BatchService {

    @Autowired
    private OrderRepository orderRepository;

    // الطلب 4 - قبل التحسين
    public int processV4Heavy() {
        List<ProductOrder> allOrders = orderRepository.findAll();
        int count = 0;

        for (ProductOrder order : allOrders) {

            Boolean processed = order.getIsProcessed();
            if (processed == null || !processed) {
                order.setIsProcessed(true);
                orderRepository.save(order);
                count++;
            }
        }
        return count;
    }

    // الطلب 4 - بعد التحسين: المعالجة على دفعات (Chunks)
    @Transactional
    public int processV4Chunks() {
        int totalProcessed = 0;
        int pageSize = 50;
        int chunkCounter = 1;

        // 1. حساب إجمالي الطلبات غير المعالجة قبل البدء (للإثبات فقط)
        long initialUnprocessed = orderRepository.countByIsProcessedFalse();
        System.out.println("--------------------------------------------------");
        System.out.println("TOTAL UNPROCESSED ORDERS FOUND: " + initialUnprocessed);
        System.out.println("--------------------------------------------------");

        // جلب أول دفعة
        Page<ProductOrder> orderPage = orderRepository.findByIsProcessedFalse(PageRequest.of(0, pageSize));

        while (orderPage.hasContent()) {
            List<ProductOrder> chunk = orderPage.getContent();

            // 2. طباعة تفاصيل الدفعة الحالية
            System.out.println(">>> [CHUNK " + chunkCounter + "] Processing " + chunk.size() + " orders...");

            for (ProductOrder order : chunk) {
                order.setIsProcessed(true);
                totalProcessed++;
            }

            // حفظ الدفعة فوراً
            orderRepository.saveAllAndFlush(chunk);

            // 3. حساب المتبقي بعد هذه الدفعة
            long remaining = orderRepository.countByIsProcessedFalse();
            System.out.println(">>> [DONE CHUNK " + chunkCounter + "] Remaining in DB: " + remaining);
            System.out.println("--------------------------------------------------");

            chunkCounter++;
            // جلب الدفعة التالية
            orderPage = orderRepository.findByIsProcessedFalse(PageRequest.of(0, pageSize));
        }

        System.out.println("FINAL RESULT: Processed " + totalProcessed + " orders successfully.");
        return totalProcessed;
    }
}