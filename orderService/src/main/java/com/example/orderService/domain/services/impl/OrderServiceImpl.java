package com.example.orderService.domain.services.impl;


import com.example.orderService.domain.clients.InventoryClient;
import com.example.orderService.domain.clients.ProductClient;
import com.example.orderService.domain.entities.Order;
import com.example.orderService.domain.entities.OrderItem;
import com.example.orderService.domain.enums.OrderStatus;
import com.example.orderService.domain.repos.OrderRepo;
import com.example.orderService.domain.services.OrderService;
import com.example.orderService.domain.services.mappers.OrderMapper;
import com.example.orderService.web.models.OrderCreateRequestDTO;
import com.example.orderService.web.models.OrderResponseDTO;
import com.example.orderService.web.models.ProductResponseDTO;
import com.example.orderService.web.models.StockResponseDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepository;
    private final OrderMapper mapper;
    private final InventoryClient inventoryClient;
    private final ProductClient productClient;

    @Override
    public OrderResponseDTO placeOrder(OrderCreateRequestDTO dto) {

        // Convert DTO → Order entity
        Order order = mapper.toEntity(dto);

        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);

        List<OrderItem> items = new ArrayList<>();

        dto.getItems().forEach(item -> {
            //fetch product details by productid

            ResponseEntity<ProductResponseDTO> product = productClient.getProduct(item.getProductId());
            if(product==null || !product.hasBody() || product.getBody()==null) throw new RuntimeException("Product not found");

            //fetch product quantity from inventory
            ResponseEntity<StockResponseDTO> stock = inventoryClient.getStock(item.getProductId());
            if(stock==null || !stock.hasBody() || stock.getBody()==null || item.getQuantity()>stock.getBody().getQuantity()) throw new RuntimeException("Stock insufficient");

            var curItem = OrderItem.builder()
                    .order(order)
                    .quantity(stock.getBody().getQuantity())
                    .price(product.getBody().price())
                    .productId(item.getProductId())
                    .build();
            items.add(curItem);


        });



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
