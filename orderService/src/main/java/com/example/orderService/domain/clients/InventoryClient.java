package com.example.orderService.domain.clients;

import com.example.orderService.web.models.StockResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("inventory")
public interface InventoryClient {
    @GetMapping("/api/stocks/{productId}")
    ResponseEntity<StockResponseDTO> getStock(@PathVariable Long productId);

}
