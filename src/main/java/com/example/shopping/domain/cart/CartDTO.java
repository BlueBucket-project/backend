package com.example.shopping.domain.cart;

import com.example.shopping.entity.cart.CartEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CartDTO {

    private Long cartId;
    private Long memberId;
    private List<CartItemDTO> cartItem;

    @Builder
    public CartDTO(Long cartId, Long memberId, List<CartItemDTO> cartItem) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.cartItem = cartItem;
    }


    public CartEntity toEntity(){
        return CartEntity.builder()
                .cartId(this.cartId)
                .memberId(this.memberId)
                .cartItem(this.cartItem.stream()
                        .map(CartItemDTO::toEntity).collect(Collectors.toList()))
                .build();

    }

}
