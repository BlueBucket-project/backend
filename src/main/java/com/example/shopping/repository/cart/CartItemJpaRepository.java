package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartStatus;
import com.example.shopping.entity.cart.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {
    CartItemEntity findByCartCartIdAndItemItemId(Long cartId, Long itemId);

    List<CartItemEntity> findByCartCartId(Long cartId);


    @Query("SELECT c FROM cartitem c JOIN FETCH c.cart WHERE c.cart.cartId = :cartId " +
            "AND c.status <> :status")
    List<CartItemEntity> findByCartCartIdAndStatusNot(@Param("cartId") Long cartId, @Param("status")CartStatus status);

    @Query("SELECT c FROM cartitem c JOIN FETCH c.cart WHERE c.cart.cartId = :cartId " +
            "AND c.status = :status")
    List<CartItemEntity> findByCartCartIdAndStatus(@Param("cartId") Long cartId, @Param("status")CartStatus status);

}
