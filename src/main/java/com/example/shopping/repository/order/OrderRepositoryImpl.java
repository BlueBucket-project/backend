package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.entity.order.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
/*
 *   writer : 오현진
 *   work :
 *          주문 레포지토리
 *          여기서는 조회 후 엔티티 반환이 아니라 DTO로 반환처리 받기 위한 곳입니다.
 *   date : 2023/10/13
 * */
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository{

    @Autowired
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public OrderDTO save(OrderDTO order) {

        OrderEntity savedOrder = orderJpaRepository.save(order.toEntity());
        return savedOrder.toDTO();
    }

    @Override
    public List<OrderDTO> findByOrderAdmin(Long memberId) {
        List<OrderEntity> adminOrder = orderJpaRepository.findByOrderAdmin(memberId);
        return adminOrder.stream().map(OrderEntity::toDTO).collect(Collectors.toList());
    }


    @Override
    public List<OrderDTO> findByOrderMember(Long memberId) {
        List<OrderEntity> memberOrder = orderJpaRepository.findByOrderMember(memberId);
        return memberOrder.stream().map(OrderEntity::toDTO).collect(Collectors.toList());
    }

}
