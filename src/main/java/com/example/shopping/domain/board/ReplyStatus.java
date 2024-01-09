package com.example.shopping.domain.board;

import io.swagger.v3.oas.annotations.media.Schema;
/*
 *   writer : YuYoHan
 *   work :
 *          관리자가 답글을 써주면 완료, 써주지 않으면 미완료입니다.
 *   date : 2023/11/20
 * */
public enum ReplyStatus {
    @Schema(description = "답글 완료")
    REPLY_O,
    @Schema(description = "답글 미완료")
    REPLY_X
}
