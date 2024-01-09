package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartMainDTO;
import com.example.shopping.domain.cart.CartStatus;
import com.example.shopping.entity.cart.CartItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
/*
 *   writer : 오현진
 *   work :
 *          장바구니 상품 레포지토리
 *          여기서는 조회 후 엔티티 반환이 아니라 DTO로 반환처리 받기 위한 곳입니다.
 *   date : 2023/11/22
 * */
@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository{

    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public CartItemDTO save(CartItemDTO cartItem) {
        CartItemEntity sample = cartItem.toEntity();
        CartItemEntity savedCartItem = cartItemJpaRepository.save(sample);
        return CartItemDTO.toDTO(savedCartItem);
    }

    @Override
    public CartMainDTO findByCartMainDTO(Long cartId, Long itemId) {
        CartItemEntity cartItem = cartItemJpaRepository.findByCartCartIdAndItemItemIdAndStatusNot(cartId, itemId, CartStatus.PURCHASED);
        if(cartItem != null){
            return CartMainDTO.toMainDTO(cartItem);
        }
        else
            return null;
    }

    @Override
    public CartItemDTO findByCartItemDTO(Long cartId, Long itemId) {
        CartItemEntity cartItem = cartItemJpaRepository.findByCartCartIdAndItemItemId(cartId, itemId);
        if(cartItem != null){
            return CartItemDTO.toDTO(cartItem);
        }
        else
            return null;
    }

    @Override
    public List<CartItemDTO> findByCartCartId(Long cartId) {
        List<CartItemEntity> items = cartItemJpaRepository.findByCartCartId(cartId);

        if(items == null)
            return null;
        else
            return items.stream().map(CartItemDTO::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CartItemDTO> findCartItemNotPurchased(Long cartId) {
        List<CartItemEntity> items = cartItemJpaRepository.findByCartCartIdAndStatusNot(cartId, CartStatus.PURCHASED);
        if(items == null)
            return null;
        else
            return items.stream().map(CartItemDTO::toDTO).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        cartItemJpaRepository.deleteById(id);
    }

    @Override
    public CartItemDTO findByCartItemId(Long cartItemId) {
        CartItemEntity item = cartItemJpaRepository.findById(cartItemId).orElseThrow();
        return CartItemDTO.toDTO(item);
    }

    @Override
    public List<CartItemDTO> findByItemId(Long itemId) {
        List<CartItemEntity> items = cartItemJpaRepository.findByItemItemId(itemId);

        if(items == null)
            return null;
        else
            return items.stream().map(CartItemDTO::toDTO).collect(Collectors.toList());
    }

}
