package com.example.shopping.domain.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/*
 *   writer : 오현진
 *   work :
 *          관리자가 상품 상태를 최종으로 바꿔주는 작업을 할 때 request DTO
 *   date : 2023/11/16
 * */
@Getter
@NoArgsConstructor
public class CartOrderDTO {

    private Long cartItemId;

    @Builder
    public CartOrderDTO(Long cartItemId) {
        this.cartItemId = cartItemId;
    }
}
