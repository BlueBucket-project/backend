package com.example.shopping.domain.member;

import io.swagger.v3.oas.annotations.media.Schema;
/*
 *   writer : 유요한
 *   work :
 *          유저인지 관리자인지 나타내는 곳
 *   date : 2023/10/13
 * */
public enum Role {
    @Schema(description = "유저")
    USER,
    @Schema(description = "관리자")
    ADMIN
}
