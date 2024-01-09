package com.example.shopping.domain.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
/*
 *   writer : YuYoHan
 *   work :
 *          게시글을 만들 때 필요한 값 즉, 제목과 내용만 받습니다.
 *   date : 2023/11/01
 * */
@Getter
@NoArgsConstructor
@ToString
public class CreateBoardDTO {
    @Schema(description = "문의글 제목", required = true)
    @NotNull(message = "문의글 제목은 필 수 입력입니다.")
    private String title;

    @Schema(description = "문의글 본문")
    private String content;

    @Builder
    public CreateBoardDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
