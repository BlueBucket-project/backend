package com.example.shopping.domain.board;

import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.board.BoardImgEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class BoardDTO {
    @Schema(description = "게시판 번호", example = "1", required = true)
    private Long boardId;

    @Schema(description = "게시판 제목", required = true)
    private String title;

    @Schema(description = "게시판 본문")
    private String content;

    @Schema(description = "유저 닉네임")
    private String nickName;

    @Schema(description = "게시글 작성 시간")
    private LocalDateTime regTime;

    @Schema(description = "게시글 이미지 정보")
    private List<BoardImgDTO> boardImgDTOList = new ArrayList<>();


    @Builder
    public BoardDTO(Long boardId,
                    String title,
                    String content,
                    String nickName,
                    LocalDateTime regTime,
                    List<BoardImgDTO> boardImgDTOList) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.nickName = nickName;
        this.regTime = regTime;
        this.boardImgDTOList = boardImgDTOList;
    }

    public static BoardDTO toBoardDTO(BoardEntity board) {
        List<BoardImgEntity> boardImgEntities = board.getBoardImgDTOList();
        List<BoardImgDTO> boardImgDTOS = new ArrayList<>();

        for(BoardImgEntity boardImgEntity : boardImgEntities) {
            BoardImgDTO boardImgDTO = BoardImgDTO.builder()
                    .boardImgId(boardImgEntity.getBoardImgId())
                    .oriImgName(boardImgEntity.getOriImgName())
                    .uploadImgName(boardImgEntity.getUploadImgName())
                    .uploadImgUrl(boardImgEntity.getUploadImgUrl())
                    .uploadImgPath(boardImgEntity.getUploadImgPath())
                    .repImgYn(boardImgEntity.getRepImgYn())
                    .build();

            // DTO로 바꿔준 것을 다시 List형식으로 바꿈
            // 다시 List로 해주는 이유는  private List<BoardImgDTO> boardImgDTOList = new ArrayList<>();
            // 여기에 넣어주기 위한 것이다.
            boardImgDTOS.add(boardImgDTO);
        }
        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .nickName(board.getMember().getNickName())
                .boardImgDTOList(boardImgDTOS)
                .regTime(LocalDateTime.now())
                .build();
    }
}
