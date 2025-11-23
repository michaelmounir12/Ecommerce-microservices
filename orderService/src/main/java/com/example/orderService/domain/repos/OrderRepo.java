package com.example.orderService.domain.repos;


import com.example.orderService.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
