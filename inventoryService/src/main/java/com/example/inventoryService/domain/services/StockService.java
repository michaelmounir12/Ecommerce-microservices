package com.example.inventoryService.domain.services;


import com.example.inventoryService.web.models.StockRequestDTO;
import com.example.inventoryService.web.models.StockResponseDTO;
import com.example.inventoryService.web.models.StockUpdateDTO;

public interface StockService {

    StockResponseDTO createStock(StockRequestDTO dto);

    StockResponseDTO getStock(Long productId);

    StockResponseDTO updateStock(StockUpdateDTO dto);

    void reduceStock(Long productId, int quantity);

    void deleteStock(Long productId);
}
