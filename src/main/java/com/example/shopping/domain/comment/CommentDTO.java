package com.example.shopping.domain.comment;

import com.example.shopping.entity.comment.CommentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class CommentDTO {
    @Schema(description = "답변 번호")
    private Long commentId;
    @Schema(description = "답변")
    private String comment;
    @Schema(description = "답변 등록 시간")
    private LocalDateTime writeTime;

    @Builder
    public CommentDTO(Long commentId, String comment, LocalDateTime writeTime) {
        this.commentId = commentId;
        this.comment = comment;
        this.writeTime = writeTime;
    }

    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        return CommentDTO.builder()
                .commentId(commentEntity.getCommentId())
                .comment(commentEntity.getComment())
                .writeTime(LocalDateTime.now())
                .build();
    }
}
