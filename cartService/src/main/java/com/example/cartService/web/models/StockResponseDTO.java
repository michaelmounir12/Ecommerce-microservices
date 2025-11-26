package com.example.cartService.web.models;


import lombok.Data;

@Data
public class StockResponseDTO {

    private Long id;
    private Long productId;
    private Integer quantity;
}
