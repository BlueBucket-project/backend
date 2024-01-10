package com.example.shopping.domain.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
