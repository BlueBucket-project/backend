package com.example.shopping.entity.board;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.BoardSecret;
import com.example.shopping.domain.board.CreateBoardDTO;
import com.example.shopping.domain.board.ReplyStatus;
import com.example.shopping.domain.comment.CommentDTO;
import com.example.shopping.entity.Base.BaseEntity;
import com.example.shopping.entity.comment.CommentEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
/*
 *   writer : 유요한
 *   work :
 *          유저 테이블을 만들어줍니다.
 *   date : 2024/01/06
 * */
@Entity(name = "board")
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long boardId;

    @Column(length = 300, nullable = false)
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Enumerated(EnumType.STRING)
    private BoardSecret boardSecret;

    @Enumerated(EnumType.STRING)
    private ReplyStatus replyStatus;


    // 댓글
    // 여기에 적용해야 합니다. 보통 게시물을 삭제해야 이미지가 삭제되므로
    // 게시물이 주축이기 때문에 여기에 cascade = CascadeType.ALL을 추가
    // orphanRemoval = true도 게시글을 삭제하면
    // 댓글도 삭제되므로 여기서 작업을 해야합니다.
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("commentId desc ")
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @Builder
    public BoardEntity(Long boardId,
                       String title,
                       String content,
                       MemberEntity member,
                       ItemEntity item,
                       BoardSecret boardSecret,
                       List<CommentEntity> commentEntityList,
                       ReplyStatus replyStatus) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.member = member;
        this.item = item;
        this.boardSecret = boardSecret;
        this.commentEntityList = commentEntityList;
        this.replyStatus = replyStatus;
    }

    // 게시글 DTO를 엔티티로 변환
    public static BoardEntity toBoardEntity(BoardDTO board,
                                            MemberEntity member,
                                            ItemEntity item) {
        BoardEntity boardEntity = BoardEntity.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .member(member)
                .boardSecret(BoardSecret.UN_LOCK)
                .item(item)
                .build();

        // 댓글 처리
        List<CommentDTO> commentDTOList = board.getCommentDTOList();

        for (CommentDTO commentDTO : commentDTOList) {
            CommentEntity commentEntity = CommentEntity.toCommentEntity(commentDTO, member, boardEntity);
            boardEntity.commentEntityList.add(commentEntity);
        }
        return boardEntity;
    }

    /* 비즈니스 로직 */
    // 게시글 작성
    public static BoardEntity createBoard(CreateBoardDTO boardDTO,
                                          MemberEntity member,
                                          ItemEntity item) {
        return com.example.shopping.entity.board.BoardEntity.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                // 본인이 작성한 글은 읽을 수 있어야하기 때문에 UN_ROCK
//                .boardSecret(BoardSecret.UN_LOCK)
                .member(member)
                .item(item)
                .replyStatus(ReplyStatus.REPLY_X)
                .build();
    }

    // 답장 상태 변화
    public void changeReply(ReplyStatus replyStatus) {
        this.replyStatus = replyStatus;
    }
    // 잠금 상태 변화
    public void changeSecret(BoardSecret boardSecret) {
        this.boardSecret = boardSecret;
    }
}
