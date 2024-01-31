package com.example.shopping.domain.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/*
 *   writer : 유요한
 *   work :
 *          댓글에 대한 정보를 업데이트할 때 사용하는 RequsetDTO
 *   date : 2023/10/11
 * */
@Getter
@ToString
@NoArgsConstructor
public class UpdateCommentDTO {
    @Schema(description = "댓글")
    private String comment;

    @Builder
    public UpdateCommentDTO(String comment) {
        this.comment = comment;
    }
}
