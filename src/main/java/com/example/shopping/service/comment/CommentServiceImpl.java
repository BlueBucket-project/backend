package com.example.shopping.service.comment;

import com.example.shopping.domain.comment.CommentDTO;
import com.example.shopping.domain.comment.ModifyCommentDTO;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.comment.CommentEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.comment.CommentRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
/*
 *   writer : 유요한
 *   work :
 *          댓글 서비스
 *          - 댓글의 생성, 삭제, 업데이트 기능이 있습니다.
 *          이렇게 인터페이스를 만들고 상속해주는 방식을 선택한 이유는
 *          메소드에 의존하지 않고 필요한 기능만 사용할 수 있게 하고 가독성과 유지보수성을 높이기 위해서 입니다.
 *   date : 2023/11/16
 * */
@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    // 댓글 등록
    @Override
    public ResponseEntity<?> save(Long boardId,
                                  ModifyCommentDTO commentDTO,
                                  String memberEmail) {
        try {
            // 회원 조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("유저 : " + findUser);
            // 게시글 조회
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("게시글 : {}", findBoard);

            if(findUser != null) {
                // 댓글 생성
                CommentEntity comment = CommentEntity.createComment(commentDTO, findUser, findBoard);

                CommentEntity saveComment = commentRepository.save(comment);
                CommentDTO returnComment = CommentDTO.toCommentDTO(saveComment);
                log.info("댓글 : " + returnComment);

                return ResponseEntity.ok().body(returnComment);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 댓글 삭제
    @Override
    public String remove(Long boardId, Long commentId, String memberEmail) {
        try {
            // 게시물 조회
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            // 댓글 조회
            CommentEntity findComment = commentRepository.findById(commentId)
                    .orElseThrow(EntityNotFoundException::new);
            // 회원 조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);

            boolean equalsEmail = findUser.getEmail().equals(findComment.getMember().getEmail());
            boolean equalsId = findComment.getBoard().getBoardId().equals(findBoard.getBoardId());

            // 해당 유저인지 체크하고 댓글을 작성한 게시글 id인지 체크
            if(equalsEmail && equalsId) {
                commentRepository.deleteById(findComment.getCommentId());
                return "댓글을 삭제했습니다.";
            } else {
                return "조건에 일치한 댓글이 아닙니다.";
            }
        } catch (Exception e) {
            return "댓글을 삭제하는데 실패했습니다.";
        }
    }


    // 댓글 수정
    @Override
    public ResponseEntity<?> update(Long boardId,
                                    Long commentId,
                                    ModifyCommentDTO commentDTO,
                                    String memberEmail) {
        try {
            // 게시물 조회
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            // 댓글 조회
            CommentEntity findComment = commentRepository.findById(commentId)
                    .orElseThrow(EntityNotFoundException::new);
            // 회원 조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);

            boolean equalsEmail = findUser.getEmail().equals(findComment.getMember().getEmail());
            boolean equalsId = findComment.getBoard().getBoardId().equals(findBoard.getBoardId());

            // 해당 유저인지 체크하고 댓글을 작성한 게시글 id인지 체크
            if(equalsEmail && equalsId) {
                // 댓글 수정
                findComment = CommentEntity.builder()
                        .commentId(findComment.getCommentId())
                        .comment(commentDTO.getComment())
                        .member(findComment.getMember())
                        .board(findComment.getBoard())
                        .build();
                CommentEntity updateComment = commentRepository.save(findComment);
                log.info("댓글 : " + updateComment);
                CommentDTO returnComment = CommentDTO.toCommentDTO(updateComment);
                return ResponseEntity.ok().body(returnComment);
            } else {
                return ResponseEntity.badRequest().body("일치하지 않습니다.");
            }
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글 수정하는데 실패했습니다.");
        }
    }
}
