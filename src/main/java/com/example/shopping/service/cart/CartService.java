package com.example.shopping.service.cart;

import com.example.shopping.domain.cart.*;

import java.util.*;

public interface CartService {

    CartItemDTO addCart(CartMainDTO cart, String email);

    String deleteCart(List<CartUpdateDTO> cartItem, String email);

    String updateCart(CartUpdateDTO cartItem, String email);

    String orderCart(List<CartOrderDTO> cartOrderList, String email);
    String cancelCartOrder(List<CartOrderDTO> cartOrderList, String email);

    List<CartItemDTO> getCartList(String email);
}
