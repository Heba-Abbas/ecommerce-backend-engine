package com.ecommerce.backend_engine.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_order")
@Data @NoArgsConstructor @AllArgsConstructor
public class ProductOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double totalPrice;
    private LocalDateTime orderDate;
    private String status;
}