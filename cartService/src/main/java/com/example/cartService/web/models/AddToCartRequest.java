package com.example.cartService.web.models;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class AddToCartRequest {

    @NotNull
    private Long productId;

    @Min(1)
    private Integer quantity;

    @NotNull
    private Double price;  // product price fetched from Product Service
}
