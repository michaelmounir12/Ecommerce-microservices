package com.example.orderService.web.models;


import lombok.Data;

@Data
public class OrderItemResponseDTO {
    private Long productId;
    private Integer quantity;
    private Double price;
}
