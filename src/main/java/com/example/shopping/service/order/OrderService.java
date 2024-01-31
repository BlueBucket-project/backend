package com.example.shopping.service.order;

import com.example.shopping.domain.order.OrderItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    List<OrderItemDTO> getOrders(String email, String user);

    Page<OrderItemDTO> getOrdersPage(Pageable pageable, String email);

}
