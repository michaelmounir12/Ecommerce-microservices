package com.example.productService.domain.services.mappers;


import com.example.productService.domain.entities.Product;
import com.example.productService.web.models.ProductRequestDTO;
import com.example.productService.web.models.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    // ProductRequestDTO → Product
    // We ignore `category` because we only map the simple fields;
    // service layer should handle setting Category object
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDTO dto);

    // Product → ProductResponseDTO
    // Map category fields if Category object exists
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponseDTO toResponseDTO(Product product);
}
