package com.example.cartService.domain.services;


import com.example.cartService.web.models.AddToCartRequest;
import com.example.cartService.web.models.CartResponse;
import com.example.cartService.web.models.UpdateCartItemRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CartService {

    CartResponse getUserCart(Long userId);

    CartResponse addToCart(Long userId, AddToCartRequest request);

    CartResponse updateCartItem(Long userId,UpdateCartItemRequest request);

    CartResponse removeCartItem(Long userId, Long productId);

    void clearCart(Long userId);
}