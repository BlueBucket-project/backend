package com.example.shopping.repository.cart;

import com.example.shopping.entity.cart.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByMemberMemberId(long mbrId);
}
