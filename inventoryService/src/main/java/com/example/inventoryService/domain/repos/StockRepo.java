package com.example.inventoryService.domain.repos;


import com.example.inventoryService.domain.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StockRepo extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductId(Long productId);
}
