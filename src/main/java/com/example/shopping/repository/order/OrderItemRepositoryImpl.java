package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.entity.order.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
/*
 *   writer : 오현진
 *   work :
 *          주문 상품 레포지토리
 *          여기서는 조회 후 엔티티 반환이 아니라 DTO로 반환처리 받기 위한 곳입니다.
 *   date : 2023/11/01
 * */
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
