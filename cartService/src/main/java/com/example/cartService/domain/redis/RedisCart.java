package com.example.cartService.domain.redis;


import lombok.*;
import java.io.Serializable;
import java.util.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RedisCart implements Serializable {

    private Long userId;
    private Double totalAmount;
    private List<RedisCartItem> items = new ArrayList<>();
}
