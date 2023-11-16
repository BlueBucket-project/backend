package com.example.shopping.domain.cart;

import io.swagger.v3.oas.annotations.media.Schema;

public enum CartStatus {
    @Schema(description = "물품대기상태")
    WAIT,
    @Schema(description ="물품예약상태")
    RESERVED,
    @Schema(description ="물품구매상태")
    PURCHASED
}
