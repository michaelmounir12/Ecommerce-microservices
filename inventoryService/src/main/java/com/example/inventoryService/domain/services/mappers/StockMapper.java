package com.example.inventoryService.domain.services.mappers;



import com.example.inventoryService.domain.entities.Stock;
import com.example.inventoryService.web.models.StockRequestDTO;
import com.example.inventoryService.web.models.StockResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {

    Stock toEntity(StockRequestDTO dto);

    StockResponseDTO toDTO(Stock stock);
}
