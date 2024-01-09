package com.example.shopping.domain.cart;

import io.swagger.v3.oas.annotations.media.Schema;
/*
 *   writer : 오현진
 *   work :
 *          장바구니 상품의 상태를 나타내고 있습니다.
 *   date : 2023/11/16
 * */
public enum CartStatus {
    @Schema(description = "물품대기상태")
    WAIT,
    @Schema(description ="물품예약상태")
    RESERVED,
    @Schema(description ="물품구매상태")
    PURCHASED
}
