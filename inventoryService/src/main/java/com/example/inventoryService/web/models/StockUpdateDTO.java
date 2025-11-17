package com.example.inventoryService.web.models;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockUpdateDTO {

    @NotNull(message = "Quantity change value is required")
    private Long productId;

    @Min(value = 0, message = "Stock cannot go negative")
    private Integer quantity;
}
