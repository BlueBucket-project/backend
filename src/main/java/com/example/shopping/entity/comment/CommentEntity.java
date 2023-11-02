package com.example.shopping.entity.comment;

import com.example.shopping.domain.comment.CommentDTO;
import com.example.shopping.domain.comment.ModifyCommentDTO;
import com.example.shopping.entity.Base.BaseEntity;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "comment")
@Table
@ToString
@Getter
@NoArgsConstructor
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
    public CommentEntity(Long commentId, String comment, MemberEntity member, BoardEntity board) {
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
    public static CommentEntity createComment(ModifyCommentDTO commentDTO,
                                              MemberEntity member,
                                              BoardEntity board) {
        return CommentEntity.builder()
                .comment(commentDTO.getComment())
                .member(member)
                .board(board)
                .build();
    }
}
