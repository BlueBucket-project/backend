package com.example.shopping.service.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartMainDTO;
import com.example.shopping.domain.cart.CartUpdateDTO;

import java.util.*;

public interface CartService {

    CartItemDTO addCart(CartMainDTO cart, String email);

    String deleteCart(List<CartUpdateDTO> cartItem, String email);

    String updateCart(CartUpdateDTO cartItem, String email);
}
