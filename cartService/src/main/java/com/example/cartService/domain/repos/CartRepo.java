package com.example.cartService.domain.repos;


import com.example.cartService.domain.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
}
