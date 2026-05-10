package com.ecommerce.backend_engine.service;

import com.ecommerce.backend_engine.entity.ProductOrder;
import com.ecommerce.backend_engine.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BatchService {
    @Autowired private OrderRepository orderRepository;

    // قبل التحسين: سحب كل البيانات
    public int processV4Heavy() {
        List<ProductOrder> allOrders = orderRepository.findAll();
        return allOrders.size();
    }

    // بعد التحسين: المعالجة على دفعات (Chunks)
    public int processV4Chunks() {
        int total = 0;
        for (int i = 0; i < 10; i++) {
            List<ProductOrder> chunk = orderRepository.findAll(PageRequest.of(i, 50)).getContent();
            if (chunk.isEmpty()) break;
            total += chunk.size();
        }
        return total;
    }
}