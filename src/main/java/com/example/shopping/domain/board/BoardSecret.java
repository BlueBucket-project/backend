package com.example.shopping.domain.board;

import io.swagger.v3.oas.annotations.media.Schema;

public enum BoardSecret {
    @Schema(description = "내글")
    UN_LOCK,
    @Schema(description = "다른 사람 글")
    LOCK
}
