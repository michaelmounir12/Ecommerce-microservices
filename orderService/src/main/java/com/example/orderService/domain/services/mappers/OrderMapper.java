package com.example.orderService.domain.services.mappers;



import com.example.orderService.domain.entities.Order;
import com.example.orderService.web.models.OrderCreateRequestDTO;
import com.example.orderService.web.models.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toEntity(OrderCreateRequestDTO dto);

    OrderResponseDTO toDTO(Order order);
}
