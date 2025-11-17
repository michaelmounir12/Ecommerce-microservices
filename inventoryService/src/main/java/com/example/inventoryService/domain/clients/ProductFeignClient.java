package com.example.inventoryService.domain.clients;


import com.example.inventoryService.web.models.CategoryResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("product")
public interface ProductFeignClient {
    @GetMapping(value = "/api/categories/{id}",consumes = "application/json")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long id);
}
