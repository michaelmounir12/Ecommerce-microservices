package com.example.productService.domain.services.mappers;



import com.example.productService.domain.entities.Category;
import com.example.productService.web.models.CategoryRequestDTO;
import com.example.productService.web.models.CategoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequestDTO dto);

    CategoryResponseDTO toResponseDTO(Category category);
}
