package com.example.shopping.service.cart;

import com.example.shopping.domain.cart.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.*;

public interface CartService {

    CartDTO addCart(CartMainDTO cart, String email);

    String deleteCart(List<UpdateCartDTO> cartItem, String email);

    String updateCart(UpdateCartDTO cartItem, String email);

    ResponseEntity<?> orderCart(List<CartOrderDTO> cartOrderList, String email);

    String cancelCartOrder(List<CartOrderDTO> cartOrderList, String email);

    List<CartItemDTO> getCartList(String email);

    Page<CartItemDTO> getCartPage(Pageable pageable, String email);
}
