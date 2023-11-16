package com.example.shopping.domain.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartOrderDTO {

    private Long cartItemId;

    @Builder
    public CartOrderDTO(Long cartItemId) {
        this.cartItemId = cartItemId;
    }
}
