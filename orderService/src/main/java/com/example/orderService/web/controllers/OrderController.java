package com.example.orderService.web.controllers;



import com.example.orderService.domain.services.OrderService;
import com.example.orderService.web.models.OrderCreateRequestDTO;
import com.example.orderService.web.models.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Place a new order",
            description = "REST API to create a new order with items"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @Valid @RequestBody OrderCreateRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(dto));
    }


    @Operation(
            summary = "Retrieve order details",
            description = "REST API to get order details by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order retrieved"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }


    @Operation(
            summary = "Update order status",
            description = "REST API to update order status (Placed, Paid, Shipped, Delivered, Cancelled)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{orderId}/status/{status}")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long orderId,
            @PathVariable String status
    ) {
        orderService.updateStatus(orderId, status);
        return ResponseEntity.ok().build();
    }
}
