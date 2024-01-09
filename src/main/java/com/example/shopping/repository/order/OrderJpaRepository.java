package com.example.shopping.repository.order;

import com.example.shopping.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
/*
 *   writer : 오현진
 *   work :
 *          주문 레포지토리
 *   date : 2023/10/13
 * */
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByOrderAdmin(Long memberId);

    List<OrderEntity> findByOrderMember(Long memberId);

}
