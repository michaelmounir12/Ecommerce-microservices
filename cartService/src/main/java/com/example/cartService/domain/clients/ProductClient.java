package com.example.cartService.domain.clients;

import com.example.cartService.web.models.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("products")
public interface ProductClient {
    @GetMapping("/api/products/{id}")
    ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id);
}