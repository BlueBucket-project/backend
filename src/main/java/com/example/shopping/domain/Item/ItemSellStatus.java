package com.example.shopping.domain.Item;

import io.swagger.v3.oas.annotations.media.Schema;
/*
 *   writer : 유요한
 *   work :
 *          상품에 대한 정보를 나타내고 있습니다.
 *   date : 2023/10/13
 * */
public enum ItemSellStatus {
    @Schema(description = "판매 중")
    SELL,
    @Schema(description = "예약")
    RESERVED,
    @Schema(description = "품절")
    SOLD_OUT
}
