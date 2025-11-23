package com.example.orderService.domain.services;


import com.example.orderService.web.models.OrderCreateRequestDTO;
import com.example.orderService.web.models.OrderResponseDTO;

public interface OrderService {

    OrderResponseDTO placeOrder(OrderCreateRequestDTO dto);

    OrderResponseDTO getOrder(Long id);

    void updateStatus(Long orderId, String status);
}
