package com.ecommerce.backend_engine.repository;

import com.ecommerce.backend_engine.entity.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Long> {

    Page<ProductOrder> findByIsProcessedFalse(Pageable pageable);
}