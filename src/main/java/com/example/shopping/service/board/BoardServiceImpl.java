package com.example.shopping.service.board;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.BoardSecret;
import com.example.shopping.domain.board.CreateBoardDTO;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.member.UserException;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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
        try {
            // 회원 조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("user : " + findUser);
            log.info("닉네임 : " + findUser.getNickName());
            // 상품 조회
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("상품 : " + ItemDTO.toItemDTO(findItem));

            if (findUser != null && findUser.getNickName() != null) {
                // 작성할 문의글(제목, 내용), 유저 정보, 해당 상품의 정보를 넘겨준다.
                BoardEntity boardEntity =
                        BoardEntity.createBoard(boardDTO, findUser, findItem);
                // 게시글은 상품의 상세페이지에 있으므로 연관관계 맺은 상품에 넣어줘야 한다.
                findItem.getBoardEntityList().add(boardEntity);
                itemRepository.save(findItem);
                BoardEntity saveBoard = boardRepository.save(boardEntity);
                BoardDTO returnBoard = BoardDTO.toBoardDTO(saveBoard, findUser.getNickName());
                log.info("게시글 : " + returnBoard);

                return ResponseEntity.ok().body(returnBoard);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 문의 삭제
    @Override
    public String removeBoard(Long boardId, String memberEmail) {
        // 게시글 조회
        BoardEntity findBoard = boardRepository.findByBoardId(boardId)
                .orElseThrow(EntityNotFoundException::new);
        // 유저 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);

        if (findUser.getEmail().equals(findBoard.getMember().getEmail())) {
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
        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);
        // 문의글 조회
        BoardEntity findBoard = boardRepository.findByBoardId(boardId)
                .orElseThrow(EntityNotFoundException::new);

        // 문의글을 작성할 때 등록된 이메일이 받아온 이메일이 맞아야 true
        if (findUser.getEmail().equals(findBoard.getMember().getEmail())) {
            BoardDTO boardDTO = BoardDTO.toBoardDTO(findBoard, findUser.getNickName());
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
            BoardEntity findBoard = boardRepository.findByBoardId(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("게시글 닉네임 : " + findBoard.getMember().getNickName());
            // 유저 조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("유저 : " + findUser);

            // 받아온 유저를 조회하고 그 유저 정보와 게시글에 담긴 유저가 일치하는지
            boolean equalsEmail = findUser.getEmail().equals(findBoard.getMember().getEmail());
            if(equalsEmail) {
                // 수정할 내용, 유저정보, 게시글을 작성할 때 받은 상품의 정보를 넘겨준다.
                findBoard = BoardEntity.builder()
                        .boardId(findBoard.getBoardId())
                        .title(boardDTO.getTitle())
                        .content(boardDTO.getContent())
                        .item(findBoard.getItem())
                        .member(findBoard.getMember())
                        .boardSecret(BoardSecret.UN_LOCK)
                        .build();
                BoardEntity updateBoard = boardRepository.save(findBoard);
                BoardDTO returnBoard = BoardDTO.toBoardDTO(updateBoard, findUser.getNickName());
                log.info("게시글 : " + returnBoard);
                return ResponseEntity.ok().body(returnBoard);
            } else {
                return ResponseEntity.badRequest().body("일치하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("수정하는데 실패했습니다. : " + e.getMessage());
        }
    }

    // 작성자의 문의글 보기
    @Transactional(readOnly = true)
    @Override
    public Page<BoardDTO> getMyBoards(String memberEmail, Pageable pageable, String searchKeyword) {

        MemberEntity findUser = memberRepository.findByEmail(memberEmail);

        Page<BoardEntity> findAllBoards;
        if (StringUtils.isNotBlank(searchKeyword)) {
            // 작성자의 문의글을 조회해온다.
            findAllBoards = boardRepository.findByMemberEmailAndTitleContaining(memberEmail, pageable, searchKeyword);
        } else {
            findAllBoards = boardRepository.findAllByMemberEmail(memberEmail, pageable);
        }
        // 작성자의 모든 글은 본인 글이니 볼 수 있도록 상태를 바꿔준다.
        findAllBoards.forEach(board -> board.changeSecret(BoardSecret.UN_LOCK));
        return findAllBoards.map(board -> BoardDTO.toBoardDTO(board, findUser.getNickName()));
    }

    // 상품에 대한 문의글 보기
    @Transactional(readOnly = true)
    @Override
    public Page<BoardDTO> getBoards(Pageable pageable,
                                    Long itemId,
                                    String searchKeyword,
                                    String email) {

        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(email);
        log.info("유저 : " + findUser);

        // 상품 조회
        ItemEntity findItem = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        log.info("상품 : " + findItem);

        // 조회해올 게시글을 넣을 곳
        Page<BoardEntity> findAllBoards;
        if (StringUtils.isNotBlank(searchKeyword)) {
            findAllBoards = boardRepository.findByItemItemIdContaining(itemId, pageable, searchKeyword);
        } else {
            findAllBoards = boardRepository.findAllByItemItemId(itemId, pageable);
        }

        for (BoardEntity boardEntity : findAllBoards) {
            if (email != null) {
                if (boardEntity.getMember().getEmail().equals(findUser.getEmail())) {
                    boardEntity.changeSecret(BoardSecret.UN_LOCK);
                } else {
                    boardEntity.changeSecret(BoardSecret.LOCK);
                }
            } else {
                boardEntity.changeSecret(BoardSecret.LOCK);
            }
        }
        log.info("조회된 게시글 수 : {}", findAllBoards.getTotalElements());
        log.info("조회된 게시글 : {}", findAllBoards);

        return findAllBoards.map(board -> BoardDTO.toBoardDTO(board, board.getMember().getNickName()));
    }
}