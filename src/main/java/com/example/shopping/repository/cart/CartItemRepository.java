package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartMainDTO;

import java.util.*;
/*
 *   writer : 오현진
 *   work :
 *          장바구니 상품을 DTO반환 받으려고 처리
 *   date : 2023/11/17
 * */
public interface CartItemRepository {

    CartItemDTO save(CartItemDTO cartItem);

    CartMainDTO findByCartMainDTO(Long cartId, Long itemId);

    CartItemDTO findByCartItemDTO(Long cartId, Long itemId);

    List<CartItemDTO> findByCartCartId(Long cartId);

    List<CartItemDTO> findCartItemNotPurchased(Long cartId);

    void delete(Long id);

    CartItemDTO findByCartItemId(Long cartItemId);

    List<CartItemDTO> findByItemId(Long itemId);
}
