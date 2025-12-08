package com.example.inventoryService.domain.services.impl;


import com.example.inventoryService.domain.entities.Stock;
import com.example.inventoryService.domain.repos.StockRepo;
import com.example.inventoryService.domain.services.StockService;
import com.example.inventoryService.domain.services.mappers.StockMapper;
import com.example.inventoryService.kafka.StockStatusEvent;
import com.example.inventoryService.kafka.StockStatusProducer;
import com.example.inventoryService.web.models.StockRequestDTO;
import com.example.inventoryService.web.models.StockResponseDTO;
import com.example.inventoryService.web.models.StockUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {


    private final StockRepo stockRepository;
    private final StockMapper mapper;
    private final StockStatusProducer stockStatusProducer;
    @Override
    public StockResponseDTO createStock(StockRequestDTO dto) {
        Stock stock = mapper.toEntity(dto);
        stockRepository.save(stock);
        return mapper.toDTO(stock);
    }

    @Override
    public StockResponseDTO getStock(Long productId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        return mapper.toDTO(stock);
    }

    @Override
    public StockResponseDTO updateStock(StockUpdateDTO dto) {
        Stock stock = stockRepository.findByProductId(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        if(stock.getQuantity()==0) stockStatusProducer.sendStockStatus(new StockStatusEvent(stock.getProductId(), "IN_STOCK"));
        stock.setQuantity(dto.getQuantity());
        stockRepository.save(stock);

        return mapper.toDTO(stock);
    }

    @Override
    public void reduceStock(Long productId, int quantity) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        if (stock.getQuantity() < quantity)
            throw new RuntimeException("Insufficient stock");

        stock.setQuantity(stock.getQuantity() - quantity);
        if(stock.getQuantity() == 0) stockStatusProducer.sendStockStatus(new StockStatusEvent(stock.getProductId(),"OUT_OF_STOCK"));
        stockRepository.save(stock);
    }

    @Override
    public void deleteStock(Long productId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        stockRepository.delete(stock);
    }
}
