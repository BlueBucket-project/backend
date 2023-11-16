package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartStatus;
import com.example.shopping.domain.member.ResponseMemberDTO;
import com.example.shopping.entity.cart.CartEntity;
import com.example.shopping.entity.cart.CartItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository{

    @Autowired
    private final CartJpaRepository cartJpaRepository;

    @Override
    public CartDTO create(CartDTO cart) {
        CartEntity newCart = CartDTO.toNewEntity(cart);

        for(CartItemDTO item: cart.getCartItems()){
            CartItemEntity newCartItem = CartItemEntity.builder()
                    .count(item.getCount())
                    .cart(item.getCart() == null? null : newCart)
                    .item(item.getItem().toEntity())
                    .status(CartStatus.WAIT)
                    .build();
            newCart.addCartItems(newCartItem);
        }

        CartEntity savedCart = cartJpaRepository.save(newCart);
        return CartDTO.toCartDTO(savedCart);    }

    @Override
    public CartDTO save(CartDTO cart) {
        CartEntity newCart = cart.toEntity();
        CartEntity savedCart = cartJpaRepository.save(newCart);
        return CartDTO.toCartDTO(savedCart);
    }

    @Override
    public CartDTO findByMemberMemberId(Long memberId) {
        Optional<CartEntity> cart = cartJpaRepository.findByMemberMemberId(memberId);
        return cart.map(CartDTO::toCartDTO).orElse(null);
    }
}
