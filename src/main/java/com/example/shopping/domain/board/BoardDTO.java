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
import java.util.stream.Collectors;

/*
 *   writer : YuYoHan
 *   work :
 *          게시글을 필요한 값을 리턴해줍니다.
 *   date : 2024/01/18
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
    public static BoardDTO toBoardDTO(BoardEntity board) {
        // 게시글 댓글 처리
        List<CommentEntity> commentEntityList =
                board.getCommentEntityList() != null ? board.getCommentEntityList() : Collections.emptyList();

        List<CommentDTO> commentDTO = commentEntityList.stream()
                .map(CommentDTO::toCommentDTO)
                .collect(Collectors.toList());

        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .nickName(board.getMember().getNickName())
                .commentDTOList(commentDTO)
                .itemId(board.getItem().getItemId())
                // 답글 미완료 상태로 등록
                .replyStatus(board.getReplyStatus())
                .regTime(board.getRegTime())
                .boardSecret(board.getBoardSecret())
                .build();
    }
}
