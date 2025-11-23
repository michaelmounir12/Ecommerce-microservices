package com.example.orderService.domain.services.impl;


import com.example.orderService.domain.entities.Order;
import com.example.orderService.domain.entities.OrderItem;
import com.example.orderService.domain.enums.OrderStatus;
import com.example.orderService.domain.repos.OrderRepo;
import com.example.orderService.domain.services.OrderService;
import com.example.orderService.domain.services.mappers.OrderMapper;
import com.example.orderService.web.models.OrderCreateRequestDTO;
import com.example.orderService.web.models.OrderResponseDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepository;
    private final OrderMapper mapper;

    @Override
    public OrderResponseDTO placeOrder(OrderCreateRequestDTO dto) {

        // Convert DTO → Order entity
        Order order = mapper.toEntity(dto);

        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);

        // Create order items
        var items = dto.getItems().stream().map(i ->
                OrderItem.builder()
                        .order(order)
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .price(0.0) // TODO: call Product Service to fetch price
                        .build()
        ).collect(Collectors.toList());

        order.setItems(items);

        // Calculate total
        double total = items.stream().mapToDouble(it -> it.getPrice() * it.getQuantity()).sum();
        order.setTotalPrice(total);

        orderRepository.save(order);

        // TODO: send "reduce stock" event to Inventory Service (Kafka)
        return mapper.toDTO(order);
    }

    @Override
    public OrderResponseDTO getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapper.toDTO(order);
    }

    @Override
    public void updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderRepository.save(order);
    }
}
