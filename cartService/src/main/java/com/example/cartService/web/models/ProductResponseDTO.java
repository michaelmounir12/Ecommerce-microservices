package com.example.cartService.web.models;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        double price,
        Long categoryId,
        String categoryName
) {}
