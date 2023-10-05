package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository{

    private final CartJpaRepository cartJpaRepository;

    @Override
    public CartDTO save(CartDTO cart) {
        //TODO - 삭제 수정 추가 전부 영속성 이용하여 작업
        return cartJpaRepository.save(cart.toEntity()).toDTO();
    }

    @Override
    public Optional<CartDTO> findByMemberId(long mbrId) {
        return cartJpaRepository.findByMeberId(mbrId);
    }
}
