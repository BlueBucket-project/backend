package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;

import java.util.*;
/*
 *   writer : 오현진
 *   work :
 *          주문을 DTO반환 받으려고 처리
 *   date : 2023/10/13
 * */
public interface OrderRepository {

    OrderDTO save(OrderDTO order);

    List<OrderDTO> findByOrderAdmin(Long memberId);

    List<OrderDTO> findByOrderMember(Long memberId);

}
