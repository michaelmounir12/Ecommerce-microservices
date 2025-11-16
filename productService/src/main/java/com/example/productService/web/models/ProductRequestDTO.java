package com.example.productService.web.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequestDTO(

        @NotBlank(message = "Product name is required")
        String name,

        @NotBlank(message = "Product description is required")
        String description,

        @Positive(message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Category ID is required")
        Long categoryId,
        Integer stock

) {}
