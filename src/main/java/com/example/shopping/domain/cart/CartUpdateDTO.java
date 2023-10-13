package com.example.shopping.domain.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CartUpdateDTO {

    @Schema(description = "장바구니번호")
    private Long cartId;

    @Schema(description = "상품번호")
    private Long itemId;

    @Schema(description = "수정 상품수량")
    private int count;

    @Builder
    public CartUpdateDTO(Long cartId, Long itemId, int count) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.count = count;
    }
}
