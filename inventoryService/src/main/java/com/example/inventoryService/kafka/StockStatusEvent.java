package com.example.inventoryService.kafka;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockStatusEvent {
    private Long productId;
    private String status; // OUT_OF_STOCK or BACK_IN_STOCK
}
