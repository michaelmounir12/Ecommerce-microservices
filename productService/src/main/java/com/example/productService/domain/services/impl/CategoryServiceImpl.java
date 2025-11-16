package com.example.productService.domain.services.impl;



import com.example.productService.domain.entities.Category;
import com.example.productService.domain.repos.CategoryRepo;
import com.example.productService.domain.services.mappers.CategoryMapper;
import com.example.productService.domain.services.mappers.CategoryService;
import com.example.productService.web.models.CategoryRequestDTO;
import com.example.productService.web.models.CategoryResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepository;
    private final CategoryMapper categoryMapper;



    @Override
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDTO getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
