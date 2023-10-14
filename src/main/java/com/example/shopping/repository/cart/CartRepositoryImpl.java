package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.entity.cart.CartEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository{

    @Autowired
    private final CartJpaRepository cartJpaRepository;

    @Override
    public CartDTO save(CartDTO cart) {
        CartEntity savedCart = cartJpaRepository.save(cart.toEntity());
        return savedCart.toDTO();
    }

    @Override
    public CartDTO findByMemberMemberId(Long memberId) {
        Optional<CartEntity> cart = cartJpaRepository.findByMemberMemberId(memberId);
        return cart.map(CartEntity::toDTO).orElse(null);
    }
}
