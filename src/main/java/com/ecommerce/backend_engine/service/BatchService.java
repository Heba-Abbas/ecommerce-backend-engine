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


        Page<ProductOrder> orderPage = orderRepository.findByIsProcessedFalse(PageRequest.of(0, pageSize));

        while (orderPage.hasContent()) {
            List<ProductOrder> chunk = orderPage.getContent();

            for (ProductOrder order : chunk) {
                order.setIsProcessed(true);
                totalProcessed++;
            }

            orderRepository.saveAll(chunk);
            orderPage = orderRepository.findByIsProcessedFalse(PageRequest.of(0, pageSize));
        }

        return totalProcessed;
    }
}