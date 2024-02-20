package com.example.shopping.domain.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/*
 *   writer : 유요한
 *   work :
 *          이메일 인증 때 받은 코드가 맞는지 체크하는 용도
 *   date : 2024/01/10
 * */
@ToString
@Getter
@NoArgsConstructor
public class CodeDTO {
    private String code;

    @Builder
    public CodeDTO(String code) {
        this.code = code;
    }
}
