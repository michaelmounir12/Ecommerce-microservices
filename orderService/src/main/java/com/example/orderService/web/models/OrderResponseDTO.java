package com.example.orderService.web.models;


import com.example.orderService.domain.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;
    private Long userId;
    private OrderStatus status;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDTO> items;
}
