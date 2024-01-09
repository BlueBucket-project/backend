package com.example.shopping.repository.order;

import com.example.shopping.entity.order.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/*
 *   writer : 유요한
 *   work :
 *          주문 상품 레포지토리
 *   date : 2023/11/01
 * */
public interface OrderItemJpaRepository extends JpaRepository<OrderItemEntity, Long> {

    List<OrderItemEntity> findByOrderOrderId(Long orderId);

    List<OrderItemEntity> findByItemSeller(Long sellerId);
}
