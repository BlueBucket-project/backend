package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import java.util.*;
/*
 *   writer : 오현진
 *   work :
 *          주문 상품을 DTO반환 받으려고 처리
 *   date : 2023/11/01
 * */
public interface OrderItemRepository {

    OrderItemDTO save(OrderItemDTO orderItem, OrderDTO order);

    List<OrderItemDTO> findByOrderOrderId(Long orderId);

    List<OrderItemDTO> findByItemSeller(Long memberId);
}
