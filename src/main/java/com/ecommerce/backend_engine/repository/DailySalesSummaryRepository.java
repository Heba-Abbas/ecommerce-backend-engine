package com.ecommerce.backend_engine.repository;

import com.ecommerce.backend_engine.entity.DailySalesSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailySalesSummaryRepository extends JpaRepository<DailySalesSummary, Long> {
    Optional<DailySalesSummary> findBySalesDate(LocalDate salesDate);
}