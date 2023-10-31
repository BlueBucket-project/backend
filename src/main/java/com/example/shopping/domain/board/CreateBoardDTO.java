package com.example.shopping.domain.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@ToString
public class CreateBoardDTO {
    @Schema(description = "게시판 제목", required = true)
    private String title;

    @Schema(description = "게시판 본문")
    private String content;

    @Builder
    public CreateBoardDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
