package com.example.inventoryService.domain.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productId; // product exists in Product Service

    @Column(nullable = false)
    private Integer quantity;
}
