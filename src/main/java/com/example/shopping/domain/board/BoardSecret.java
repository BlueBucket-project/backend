package com.example.shopping.domain.board;

import io.swagger.v3.oas.annotations.media.Schema;
/*
 *   writer : YuYoHan
 *   work :
 *          내 글이면 볼 수 있고 다른 사람의 글은 잠금 처리해서
 *          보지 못하도록 enum으로 처리해주고 있습니다.
 *   date : 2023/11/15
 * */
public enum BoardSecret {
    @Schema(description = "내글")
    UN_LOCK,
    @Schema(description = "다른 사람 글")
    LOCK
}
