package com.example.cartService.domain.redis;


import lombok.*;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RedisCartItem implements Serializable {
    private Long productId;
    private Integer quantity;
    private Double price;
}
