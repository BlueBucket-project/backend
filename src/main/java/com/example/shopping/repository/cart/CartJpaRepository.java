package com.example.shopping.repository.cart;

import com.example.shopping.entity.cart.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/*
 *   writer : 오현진, 유요한
 *   work :
 *          장바구니 레포지토리
 *   date : 2023/12/07
 * */
public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByMemberMemberId(long mbrId);
    void deleteAllByMemberMemberId(Long memberId);
}
