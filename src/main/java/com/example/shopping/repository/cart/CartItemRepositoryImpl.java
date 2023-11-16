package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartMainDTO;
import com.example.shopping.entity.cart.CartItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository{

    @Autowired
    private final CartJpaRepository cartJpaRepository;
    @Autowired
    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public CartItemDTO save(CartItemDTO cartItem) {
        CartItemEntity savedCartItem = cartItemJpaRepository.save(cartItem.toEntity());
        return CartItemDTO.toDTO(savedCartItem);
    }

    @Override
    public CartMainDTO findByCartMainDTO(Long cartId, Long itemId) {
        CartItemEntity cartItem = cartItemJpaRepository.findByCartCartIdAndItemItemId(cartId, itemId);
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
    public void delete(Long id) {
        cartItemJpaRepository.deleteById(id);
    }

    @Override
    public CartItemDTO findByCartItemId(Long cartItemId) {
        CartItemEntity item = cartItemJpaRepository.findById(cartItemId).orElseThrow();
        return CartItemDTO.toDTO(item);
    }

}
