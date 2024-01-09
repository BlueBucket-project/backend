package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartDTO;
/*
 *   writer : 오현진
 *   work :
 *          장바구니를 DTO반환 받으려고 처리
 *   date : 2023/11/16
 * */
public interface CartRepository {

    CartDTO create(CartDTO cart);

    CartDTO save(CartDTO cart);

    CartDTO findByMemberMemberId(Long memberId);

}
