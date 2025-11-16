package com.example.productService.domain.services;



import com.example.productService.web.models.ProductRequestDTO;
import com.example.productService.web.models.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO create(ProductRequestDTO dto);

    ProductResponseDTO update(Long id, ProductRequestDTO dto);

    ProductResponseDTO getById(Long id);

    List<ProductResponseDTO> getByCategory(Long categoryId);

    List<ProductResponseDTO> getAll();

    void delete(Long id);
}
