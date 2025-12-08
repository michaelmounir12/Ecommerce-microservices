package com.example.productService.kafka;


import com.example.productService.domain.entities.Product;
import com.example.productService.domain.enums.ProductStatus;
import com.example.productService.domain.repos.ProductRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockStatusConsumer {

    private final ObjectMapper objectMapper;
    private final ProductRepo productRepo;

    @KafkaListener(topics = "stock-status-topic", groupId = "product-service-group")
    public void consumeStockStatus(String message) {
        try {
            StockStatusEvent event = objectMapper.readValue(message, StockStatusEvent.class);

            log.info("Received stock status event: {}", event);

            // Process the event based on status
            switch (event.getStatus()) {

                case "IN_STOCK":
                    handleInStock(event);
                    break;
                case "OUT_OF_STOCK":
                    handleOutOfStock(event);
                    break;
                default:
                    log.warn("Unknown status: {}", event.getStatus());
            }

        } catch (Exception e) {
            log.error("Error processing stock status message: {}", message, e);
        }
    }


    private void handleInStock(StockStatusEvent event) {
        log.info("Product {} back in stock", event.getProductId());
        Product product = productRepo.findById(event.getProductId()).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        product.setStatus(ProductStatus.IN_STOCK);
        productRepo.save(product);


    }
    private void handleOutOfStock(StockStatusEvent event) {
        log.info("Product {} is out of stock", event.getProductId());
        // Your business logic here
        // e.g., notify customers, cancel pending orders, etc.
        Product product = productRepo.findById(event.getProductId()).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        product.setStatus(ProductStatus.OUT_OF_STOCK);
        productRepo.save(product);
    }
}