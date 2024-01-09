package com.example.shopping.service.board;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.BoardSecret;
import com.example.shopping.domain.board.CreateBoardDTO;
import com.example.shopping.domain.board.ReplyStatus;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.board.BoardException;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
/*
 *   writer : 유요한
 *   work :
 *          게시글 서비스
 *          - 게시글의 등록, 수정, 삭제, 그리고 작성자의 문의글과 특정 상품에 해당하는 문의글을 확인하는 기능이 있습니다.
 *          이렇게 인터페이스를 만들고 상속해주는 방식을 선택한 이유는
 *          메소드에 의존하지 않고 필요한 기능만 사용할 수 있게 하고 가독성과 유지보수성을 높이기 위해서 입니다.
 *   date : 2024/01/09
 * */
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
                boardEntity.changeSecret(BoardSecret.UN_LOCK);
                BoardEntity saveBoard = boardRepository.save(boardEntity);
                BoardDTO returnBoard = BoardDTO.toBoardDTO(
                        saveBoard,
                        findUser.getNickName(),
                        findItem.getItemId());
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
    public String removeBoard(Long boardId, UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        log.info("유저 : " + userEmail);

        // 권한 가져오기
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // 게시글 조회
        BoardEntity findBoard = boardRepository.findByBoardId(boardId)
                .orElseThrow(EntityNotFoundException::new);
        // 유저 조회
        MemberEntity findUser = memberRepository.findByEmail(userEmail);
        boolean equalsEmail = findUser.getEmail().equals(findBoard.getMember().getEmail());
        String authority = authorities.iterator().next().getAuthority();
        log.info("권한 : " + authority);

        // 일치하다면 내 글이 맞으므로 삭제할 수 있다.
        if (equalsEmail) {
            // 게시글 삭제
            boardRepository.deleteById(boardId);
            return "게시글을 삭제했습니다.";
            // 관리자 등급이 맞다면 삭제할 수 있다.
        } else if(authority.equals("ADMIN") || authority.equals("ROLE_ADMIN")){
            boardRepository.deleteById(findBoard.getBoardId());
            return "게시글을 삭제 했습니다.";
        } else {
            return "삭제할 수 없습니다.";
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
            BoardDTO boardDTO = BoardDTO.toBoardDTO(
                    findBoard,
                    findBoard.getMember().getNickName(),
                    findBoard.getItem().getItemId());
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
            boolean equalsEmail = findBoard.getMember().getEmail().equals(memberEmail);
            if(equalsEmail) {
                // 수정할 내용, 유저정보, 게시글을 작성할 때 받은 상품의 정보를 넘겨준다.
                findBoard = BoardEntity.builder()
                        .boardId(findBoard.getBoardId())
                        .title(boardDTO.getTitle() != null ? boardDTO.getTitle() : findBoard.getTitle())
                        .content(boardDTO.getContent() != null ? boardDTO.getContent() : findBoard.getContent())
                        .item(findBoard.getItem())
                        .member(findBoard.getMember())
                        .commentEntityList(findBoard.getCommentEntityList())
                        .boardSecret(BoardSecret.LOCK)
                        .build();
                BoardEntity updateBoard = boardRepository.save(findBoard);
                BoardDTO returnBoard = BoardDTO.toBoardDTO(
                        updateBoard,
                        findUser.getNickName(),
                        findBoard.getItem().getItemId());
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

        // 작성자의 문의글을 조회해온다.
        Page<BoardEntity> findAllBoards = boardRepository.findByMemberEmailAndTitleContaining(memberEmail, pageable, searchKeyword);
        // 댓글이 있으면 답변완료, 없으면 미완료
        for(BoardEntity boardCheck : findAllBoards) {
            if(boardCheck.getCommentEntityList().isEmpty()) {
                boardCheck.changeReply(ReplyStatus.REPLY_X);
            } else {
                boardCheck.changeReply(ReplyStatus.REPLY_O);
            }
        }

        // 해당 게시글을 만들때 id와 조회한 id를 체크
        // 그리고 맞다면 읽을 권한주고 없으면 잠가주기
        findAllBoards.forEach(board -> {
            if(board.getMember().getMemberId().equals(findUser.getMemberId())) {
                board.changeSecret(BoardSecret.UN_LOCK);
            } else {
                throw new BoardException("작성자의 글이 아닙니다.");
            }
        });
        return findAllBoards.map(board -> BoardDTO.toBoardDTO(
                board,
                board.getMember().getNickName(),
                board.getItem().getItemId()));
    }

    // 상품에 대한 문의글 보기
    @Transactional(readOnly = true)
    @Override
    public Page<BoardDTO> getBoards(Pageable pageable,
                                    Long itemId,
                                    String email) {

        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(email);
        log.info("유저 : " + findUser);

        // 상품 조회
        ItemEntity findItem = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        log.info("상품 : " + findItem);

        // 조회해올 게시글을 넣을 곳
        Page<BoardEntity> findAllBoards = boardRepository.findAllByItemItemId(itemId, pageable);
        // 댓글이 있으면 답변완료, 없으면 미완료
        for(BoardEntity boardCheck : findAllBoards) {
            if(boardCheck.getCommentEntityList().isEmpty()) {
                boardCheck.changeReply(ReplyStatus.REPLY_X);
            } else {
                boardCheck.changeReply(ReplyStatus.REPLY_O);
            }
        }

        for (BoardEntity boardEntity : findAllBoards) {
            // 파라미터로 받아온 이메일이 있다면
            if (email != null) {
                // 해당 게시글을 만들때 이메일과 조회한 이메일을 체크
                // 그리고 맞다면 읽을 권한주고 없으면 잠가주기
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

        return findAllBoards.map(board -> BoardDTO.toBoardDTO(
                board,
                board.getMember().getNickName(),
                board.getItem().getItemId()));
    }
}