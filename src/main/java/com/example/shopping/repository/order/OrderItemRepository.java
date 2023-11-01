package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import java.util.*;

public interface OrderItemRepository {

    OrderItemDTO save(OrderItemDTO orderItem, OrderDTO order);

    List<OrderItemDTO> findByOrderOrderId(Long orderId);

    List<OrderItemDTO> findByItemSeller(Long memberId);
}
