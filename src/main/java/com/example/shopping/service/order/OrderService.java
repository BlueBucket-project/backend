package com.example.shopping.service.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.domain.order.OrderMainDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    //adminService로 이동
    //OrderDTO orderItem(List<OrderMainDTO> order, String adminEmail);

    List<OrderItemDTO> getOrders(String email);

}
