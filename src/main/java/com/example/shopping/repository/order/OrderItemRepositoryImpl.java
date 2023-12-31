package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.entity.order.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor

public class OrderItemRepositoryImpl implements OrderItemRepository{

    @Autowired
    private final OrderItemJpaRepository orderItemJpaRepository;

    @Override
    public OrderItemDTO save(OrderItemDTO orderItem, OrderDTO order) {
        OrderItemEntity savedOrderItem = orderItemJpaRepository.save(orderItem.toOrderEntity(order.toEntity()));
        return savedOrderItem.toOrderItemDTO();
    }


    @Override
    public List<OrderItemDTO> findByOrderOrderId(Long orderId) {
        List<OrderItemEntity> findOrderItem = orderItemJpaRepository.findByOrderOrderId(orderId);
        if(findOrderItem == null)
            throw new EntityNotFoundException();
        return findOrderItem.stream().map(OrderItemEntity::toOrderItemDTO).collect(Collectors.toList());
    }


    @Override
    public List<OrderItemDTO> findByItemSeller(Long memberId) {
        List<OrderItemEntity> findSellerItem = orderItemJpaRepository.findByItemSeller(memberId);
        return findSellerItem.stream().map(OrderItemEntity::toOrderItemDTO).collect(Collectors.toList());
    }

}
