package com.example.shopping.domain.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/*
 *   writer : 오현진
 *   work :
 *          장바구니를 업데이트할 때 필요한 정보를 보내주고 있습니다.
 *   date : 2023/11/16
 * */
@Getter
@NoArgsConstructor
public class UpdateCartDTO {

    @Schema(description = "장바구니번호")
    private Long cartId;

    @Schema(description = "상품번호")
    private Long itemId;

    @Schema(description = "수정 상품수량")
    private int count;

    @Builder
    public UpdateCartDTO(Long cartId, Long itemId, int count) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.count = count;
    }
}
