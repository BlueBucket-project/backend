package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartMainDTO;
import com.example.shopping.domain.cart.CartStatus;

import java.util.*;

public interface CartItemRepository {

    CartItemDTO save(CartItemDTO cartItem);

    CartMainDTO findByCartMainDTO(Long cartId, Long itemId);

    CartItemDTO findByCartItemDTO(Long cartId, Long itemId);

    List<CartItemDTO> findByCartCartId(Long cartId);

    List<CartItemDTO> findCartItemWithStatus(Long cartId, CartStatus status);

    List<CartItemDTO> findCartItemNotPurchased(Long cartId);

    void delete(Long id);

    CartItemDTO findByCartItemId(Long cartItemId);
}
