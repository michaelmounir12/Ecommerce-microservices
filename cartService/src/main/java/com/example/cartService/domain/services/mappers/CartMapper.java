package com.example.cartService.domain.services.mappers;

import com.example.cartService.domain.entities.Cart;
import com.example.cartService.domain.redis.RedisCart;
import com.example.cartService.domain.redis.RedisCartItem;
import com.example.cartService.web.models.CartItemResponse;
import com.example.cartService.web.models.CartResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
    public static CartResponse mapDbToResponse(Cart cart) {
        return CartResponse.builder()
                .userId(cart.getUserId())
                .totalAmount(cart.getTotalAmount())
                .items(cart.getItems().stream()
                        .map(i -> new CartItemResponse(i.getId(), i.getProductId(), i.getQuantity(), i.getPrice()))
                        .toList())
                .build();
    }
    public static CartResponse mapRedisToResponse(RedisCart cart) {
        return CartResponse.builder()
                .userId(cart.getUserId())
                .totalAmount(cart.getTotalAmount())
                .items(cart.getItems().stream()
                        .map(i -> new CartItemResponse(null, i.getProductId(), i.getQuantity(), i.getPrice()))
                        .toList())
                .build();
    }
    public static RedisCart mapDbToRedis(Cart cart) {
        return RedisCart.builder()
                .userId(cart.getUserId())
                .totalAmount(cart.getTotalAmount())
                .items(cart.getItems().stream()
                        .map(i -> new RedisCartItem(i.getProductId(), i.getQuantity(), i.getPrice()))
                        .toList())
                .build();
    }

}
