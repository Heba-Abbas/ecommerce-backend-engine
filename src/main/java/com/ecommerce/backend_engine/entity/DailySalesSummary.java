package com.ecommerce.backend_engine.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_sales_summary")
@Data @NoArgsConstructor @AllArgsConstructor
public class DailySalesSummary {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate salesDate;
    private Integer totalItemsSold;
    private Double totalRevenue;
    private Integer processedOrdersCount;
}