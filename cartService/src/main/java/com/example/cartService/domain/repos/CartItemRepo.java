package com.example.cartService.domain.repos;


import com.example.cartService.domain.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
}
