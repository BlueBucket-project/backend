package com.example.shopping.entity.comment;

import com.example.shopping.domain.comment.CommentDTO;
import com.example.shopping.domain.comment.UpdateCommentDTO;
import com.example.shopping.entity.Base.BaseEntity;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.*;

import javax.persistence.*;
/*
 *   writer : 유요한
 *   work :
 *          주소에 대한 ResponseDTO
 *   date : 2024/02/07
 * */
@Entity(name = "comment")
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "board"})
public class CommentEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    // 양방향으로 연관관계를 맺을 때 한 곳에서만
    // LAZY를 해도 반대쪽에서도 적용이 되므로
    // 그곳에서는 추가할 필요가 없다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @Builder
    public CommentEntity(Long commentId,
                         String comment,
                         MemberEntity member,
                         BoardEntity board) {
        this.commentId = commentId;
        this.comment = comment;
        this.member = member;
        this.board = board;
    }

    // 댓글 DTO를 엔티티로 변환
    public static CommentEntity toCommentEntity(CommentDTO commentDTO,
                                                MemberEntity member,
                                                BoardEntity board) {
        return CommentEntity.builder()
                .commentId(commentDTO.getCommentId())
                .comment(commentDTO.getComment())
                .member(member)
                .board(board)
                .build();
    }

    // 생성
    public static CommentEntity createComment(UpdateCommentDTO commentDTO,
                                              MemberEntity member,
                                              BoardEntity board) {
        return CommentEntity.builder()
                .comment(commentDTO.getComment())
                .member(member)
                .board(board)
                .build();
    }

    // 수정
    public void updateComment(UpdateCommentDTO commentDTO) {
        this.comment = commentDTO.getComment() != null ? commentDTO.getComment() : this.comment;
    }
}
