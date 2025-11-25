package com.example.cartService.web.models;


import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class CartItemResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;
}
