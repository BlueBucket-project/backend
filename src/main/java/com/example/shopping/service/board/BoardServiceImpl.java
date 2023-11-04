package com.example.shopping.service.board;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.CreateBoardDTO;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.comment.CommentEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class BoardServiceImpl implements BoardService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    // 문의 등록 메소드
    @Override
    public ResponseEntity<?> saveBoard(Long itemId,
                                       CreateBoardDTO boardDTO,
                                       String memberEmail) throws Exception {
        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);
        log.info("user : " + findUser);
        // 상품 조회
        ItemEntity findItem = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        log.info("item : " + findItem);

        if (findUser != null) {
            // 작성할 문의글(제목, 내용), 유저 정보, 해당 상품의 정보를 넘겨준다.
            BoardEntity boardEntity = BoardEntity.createBoard(boardDTO, findUser, findItem);

            BoardEntity saveBoard = boardRepository.save(boardEntity);
            BoardDTO returnBoard = BoardDTO.toBoardDTO(saveBoard);
            return ResponseEntity.ok().body(returnBoard);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
        }
    }

    // 문의 삭제
    @Override
    public String removeBoard(Long boardId, String memberEmail) {
        // 게시글 조회
        BoardEntity findBoard = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        // 유저 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);

        if(findUser.getEmail().equals(findBoard.getMember().getEmail())) {
            // 게시글 삭제
            boardRepository.deleteById(boardId);
            return "게시글을 삭제했습니다.";
        } else {
            return "일치하지 않습니다.";
        }
    }

    // 문의글을 상세 보기
    // 내 글이 아닌 경우 읽을 수 없다.
    // 상품 안에 있는 문의글은 클릭해서 들어가야 한다.
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<?> getBoard(Long boardId, String memberEmail) {
        // 유저 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);
        log.info("유저 : " + findUser);
        // 문의글 조회
        BoardEntity findBoard = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        // 문의글을 작성할 때 등록된 이메일이 받아온 이메일이 맞아야 true
        if(findUser.getEmail().equals(findBoard.getMember().getEmail())) {
            BoardDTO boardDTO = BoardDTO.toBoardDTO(findBoard);
            return ResponseEntity.ok().body(boardDTO);
        } else {
            return ResponseEntity.badRequest().body("해당 유저의 문의글이 아닙니다.");
        }
    }

    // 문의 수정
    @Override
    public ResponseEntity<?> updateBoard(Long boardId,
                                         CreateBoardDTO boardDTO,
                                         String memberEmail) {
        try {
            // 게시글 조회
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("게시글 : " + findBoard);
            // 유저 조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("유저 : " + findUser);
            // 댓글 가져오기
            List<CommentEntity> commentEntityList = findBoard.getCommentEntityList();

            // 게시글을 등록한 이메일이 맞다면 true
            if (findUser.getEmail().equals(findBoard.getMember().getEmail())) {
                for (CommentEntity commentEntity : commentEntityList) {
                    // 수정할 내용, 유저정보, 게시글을 작성할 때 받은 상품의 정보를 넘겨준다.
                    findBoard = BoardEntity.updateBoard(boardDTO, findUser, findBoard.getItem());
                    if(commentEntity != null) {
                        // 댓글을 추가해준다.
                        findBoard.getCommentEntityList().add(commentEntity);
                    } else {
                        findBoard.getCommentEntityList().add(null);
                    }
                }
            }
            BoardEntity saveBoard = boardRepository.save(findBoard);
            BoardDTO returnBoard = BoardDTO.toBoardDTO(saveBoard);
            return ResponseEntity.ok().body(returnBoard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}