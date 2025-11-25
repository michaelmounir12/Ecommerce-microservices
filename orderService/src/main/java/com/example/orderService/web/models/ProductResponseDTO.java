package com.example.orderService.web.models;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        double price,
        Long categoryId,
        String categoryName
) {}
