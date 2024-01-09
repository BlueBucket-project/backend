package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartStatus;
import com.example.shopping.entity.cart.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;
/*
 *   writer : 오현진
 *   work :
 *          장바구니 상품 레포지토리
 *          Spring Data JPA 방식을 사용하였고 fetch Join을 위하여
 *          JPQL을 사용했습니다.
 *   date : 2023/12/04
 * */
public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {

    CartItemEntity findByCartCartIdAndItemItemIdAndStatusNot(Long cartId,
                                                             Long itemId,
                                                             CartStatus status);
    CartItemEntity findByCartCartIdAndItemItemId(Long cartId, Long itemId);

    List<CartItemEntity> findByCartCartId(Long cartId);


    @Query("SELECT c FROM cartitem c JOIN FETCH c.cart WHERE c.cart.cartId = :cartId " +
            "AND c.status <> :status")
    List<CartItemEntity> findByCartCartIdAndStatusNot(@Param("cartId") Long cartId,
                                                      @Param("status")CartStatus status);

    @Query("SELECT c FROM cartitem c JOIN FETCH c.cart WHERE c.cart.cartId = :cartId " +
            "AND c.status = :status")
    List<CartItemEntity> findByCartCartIdAndStatus(@Param("cartId") Long cartId,
                                                   @Param("status")CartStatus status);

    List<CartItemEntity> findByItemItemId(Long itemId);
}
