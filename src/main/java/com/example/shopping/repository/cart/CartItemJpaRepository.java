package com.example.shopping.repository.cart;

import com.example.shopping.entity.cart.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {
    CartItemEntity findByCartCartIdAndItemItemId(Long cartId, Long itemId);

    List<CartItemEntity> findByCartCartId(Long cartId);

    @Query(value = "select * from cartitem c " +
            "where c.cart_id = :cartId and c.item_status != 'PURCHASED'", nativeQuery = true)
    List<CartItemEntity> findByCartCartIdAndStatus(@Param("cartId") Long cartId);


}
