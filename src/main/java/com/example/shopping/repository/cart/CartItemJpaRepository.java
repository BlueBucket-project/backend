package com.example.shopping.repository.cart;

import com.example.shopping.entity.cart.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {
    CartItemEntity findByCartCartIdAndItemItemId(Long cartId, Long itemId);

    List<CartItemEntity> findByCartCartId(Long cartId);
}
