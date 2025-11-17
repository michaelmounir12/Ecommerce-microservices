package com.example.inventoryService.web.controllers;



import com.example.inventoryService.domain.services.StockService;
import com.example.inventoryService.web.models.StockRequestDTO;
import com.example.inventoryService.web.models.StockResponseDTO;
import com.example.inventoryService.web.models.StockUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @Operation(
            summary = "Create stock entry",
            description = "Creates stock for a specific product"
    )
    @ApiResponse(responseCode = "201", description = "Stock created")
    @PostMapping
    public ResponseEntity<StockResponseDTO> create(
            @Valid @RequestBody StockRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockService.createStock(dto));
    }

    @Operation(
            summary = "Get stock by productId",
            description = "Returns stock quantity for a given product"
    )
    @ApiResponse(responseCode = "200", description = "Stock retrieved")
    @GetMapping("/{productId}")
    public ResponseEntity<StockResponseDTO> get(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getStock(productId));
    }

    @Operation(
            summary = "Update stock value",
            description = "Updates stock quantity for a product"
    )
    @ApiResponse(responseCode = "200", description = "Stock updated")
    @PutMapping
    public ResponseEntity<StockResponseDTO> update(
            @Valid @RequestBody StockUpdateDTO dto
    ) {
        return ResponseEntity.ok(stockService.updateStock(dto));
    }

    @Operation(
            summary = "Reduce stock after purchase",
            description = "Used by Order Service to reduce stock when order is placed"
    )
    @ApiResponse(responseCode = "200", description = "Stock reduced")
    @PostMapping("/reduce/{productId}/{quantity}")
    public ResponseEntity<Void> reduceStock(
            @PathVariable Long productId,
            @PathVariable int quantity
    ) {
        stockService.reduceStock(productId, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Delete stock entry",
            description = "Removes stock row for a specific product"
    )
    @ApiResponse(responseCode = "200", description = "Stock deleted")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        stockService.deleteStock(productId);
        return ResponseEntity.ok().build();
    }
}
