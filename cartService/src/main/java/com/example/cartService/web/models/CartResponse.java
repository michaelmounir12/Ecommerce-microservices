package com.example.cartService.web.models;


import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class CartResponse {
    private Long userId;
    private Double totalAmount;
    private List<CartItemResponse> items;
}
