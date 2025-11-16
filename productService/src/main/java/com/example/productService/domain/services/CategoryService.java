package com.example.productService.domain.services.mappers;



import com.example.productService.web.models.CategoryRequestDTO;
import com.example.productService.web.models.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO create(CategoryRequestDTO dto);

    CategoryResponseDTO getById(Long id);

    List<CategoryResponseDTO> getAll();

    void delete(Long id);
}
