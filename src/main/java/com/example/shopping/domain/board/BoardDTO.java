package com.example.shopping.domain.board;

import com.example.shopping.domain.comment.CommentDTO;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.comment.CommentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
 *   writer : YuYoHan
 *   work :
 *          게시글을 필요한 값을 리턴해줍니다.
 *   date : 2023/11/23
 * */
@Getter
@ToString
@NoArgsConstructor
public class BoardDTO {
    @Schema(description = "문의글 번호", example = "1", required = true)
    private Long boardId;

    @Schema(description = "문의글 제목", required = true)
    @NotNull(message = "문의글 제목은 필 수 입력입니다.")
    private String title;

    @Schema(description = "문의글 본문")
    private String content;

    @Schema(description = "유저 닉네임")
    private String nickName;

    @Schema(description = "문의글 작성 시간")
    private LocalDateTime regTime;

    @Schema(description = "관리자 답변")
    private List<CommentDTO> commentDTOList = new ArrayList<>();

    @Schema(description = "문의글이 본인글인지 확인")
    private BoardSecret boardSecret;

    @Schema(description = "상품 번호")
    private Long itemId;

    @Schema(description = "답글상태")
    private ReplyStatus replyStatus;


    @Builder
    public BoardDTO(Long boardId,
                    String title,
                    String content,
                    String nickName,
                    LocalDateTime regTime,
                    List<CommentDTO> commentDTOList,
                    BoardSecret boardSecret,
                    Long itemId,
                    ReplyStatus replyStatus) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.nickName = nickName;
        this.regTime = regTime;
        this.commentDTOList = commentDTOList;
        this.boardSecret = boardSecret;
        this.itemId = itemId;
        this.replyStatus = replyStatus;
    }

    // 엔티티를 DTO로 변환하는 작업
    public static BoardDTO toBoardDTO(BoardEntity board,
                                      String nickName,
                                      Long itemId) {
        // 게시글 댓글 처리
        List<CommentEntity> commentEntityList = board.getCommentEntityList();
        List<CommentDTO> commentDTOS = new ArrayList<>();

        // 엔티티 댓글을 DTO 리스트에 담아주는 작업을 진행하고 있다.
        if (commentEntityList != null) {
            for (CommentEntity commentEntity : commentEntityList) {
                CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity);
                commentDTOS.add(commentDTO);
            }
        } else {
            // commentEntityList가 null일 경우 빈 리스트로 초기화
            commentDTOS = Collections.emptyList();
        }

        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .nickName(nickName)
                .commentDTOList(commentDTOS)
                .itemId(itemId)
                // 답글 미완료 상태로 등록
                .replyStatus(board.getReplyStatus())
                .regTime(board.getRegTime())
                .boardSecret(board.getBoardSecret())
                .build();
    }

    public void viewPermission(BoardSecret boardSecret) {
        this.boardSecret = boardSecret;
    }
}
