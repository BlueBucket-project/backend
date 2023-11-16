package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartDTO;

public interface CartRepository {

    CartDTO create(CartDTO cart);

    CartDTO save(CartDTO cart);

    CartDTO findByMemberMemberId(Long memberId);

}
