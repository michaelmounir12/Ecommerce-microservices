package com.example.productService.domain.services.impl;

import com.example.productService.domain.entities.Category;
import com.example.productService.domain.entities.Product;
import com.example.productService.domain.repos.CategoryRepo;
import com.example.productService.domain.repos.ProductRepo;
import com.example.productService.domain.services.ProductService;
import com.example.productService.domain.services.mappers.ProductMapper;
import com.example.productService.web.models.ProductRequestDTO;
import com.example.productService.web.models.ProductResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepository;
    private final CategoryRepo categoryRepository;
    private final ProductMapper productMapper;


    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = productMapper.toEntity(dto);

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setCategory(category);

        return productMapper.toResponseDTO(productRepository.save(product));
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setPrice( dto.price());
        existing.setStock(dto.stock());

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existing.setCategory(category);

        return productMapper.toResponseDTO(productRepository.save(existing));
    }

    @Override
    public ProductResponseDTO getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toResponseDTO(product);
    }

    @Override
    public List<ProductResponseDTO> getByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
