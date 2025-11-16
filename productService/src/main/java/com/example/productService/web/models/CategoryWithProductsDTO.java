package com.example.productService.web.models;

import java.util.List;

public record CategoryWithProductsDTO(
        Long id,
        String name,
        List<ProductResponseDTO> products
) {}
