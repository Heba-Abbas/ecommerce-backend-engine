package com.ecommerce.backend_engine.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Data @NoArgsConstructor @AllArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer stock;
    private Double price;
}