package com.example.shopping.service.board;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.CreateBoardDTO;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.comment.CommentEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.board.BoardImgRepository;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.service.s3.S3BoardImgUploaderService;
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
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    private final S3BoardImgUploaderService s3BoardImgUploaderService;

    // 게시글 등록 메소드
    @Override
    public ResponseEntity<?> saveBoard(CreateBoardDTO boardDTO,
//                                       List<MultipartFile> boardFiles,
                                       String memberEmail) throws Exception {
        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);
        log.info("user : " + findUser);

        if (findUser != null) {
            BoardEntity boardEntity = BoardEntity.createBoard(boardDTO, findUser);

            /*  이미지를 추가할 경우를 대비해서 주석처리 */
//            log.info("게시글 : " + boardEntity);
//            List<BoardImgDTO> boardImg = s3BoardImgUploaderService.upload("board", boardFiles);
//
//            for (int i = 0; i < boardImg.size(); i++) {
//                BoardImgDTO boardImgDTO = boardImg.get(i);
//                log.info("게시글 이미지 : " + boardImgDTO);
//
//                BoardImgEntity boardImgEntity = BoardImgEntity.builder()
//                        .oriImgName(boardImgDTO.getOriImgName())
//                        .uploadImgUrl(boardImgDTO.getUploadImgUrl())
//                        .uploadImgPath(boardImgDTO.getUploadImgPath())
//                        .uploadImgName(boardImgDTO.getUploadImgName())
//                        .board(boardEntity)
//                        .repImgYn(i == 0 ? "Y" : "N")
//                        .build();
//
//                boardEntity.getBoardImgEntityList().add(boardImgEntity);
//            }
            BoardEntity saveBoard = boardRepository.save(boardEntity);
            BoardDTO returnBoard = BoardDTO.toBoardDTO(saveBoard);
            return ResponseEntity.ok().body(returnBoard);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
        }
    }

    // 게시글 삭제
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

    // 전체 게시글 보기
    @Transactional(readOnly = true)
    @Override
    public Page<BoardDTO> getBoards(Pageable pageable) {
        Page<BoardEntity> allBoard = boardRepository.findAll(pageable);
        log.info("모든 게시글 : {}", allBoard );
        return allBoard.map(BoardDTO::toBoardDTO);
    }

    // 검색
    @Transactional(readOnly = true)
    @Override
    public Page<BoardDTO> getSearchBoards(Pageable pageable, String searchKeyword) {
        Page<BoardEntity> searchBoard = boardRepository.findByTitleContaining(pageable, searchKeyword);
        log.info("검색한 게시글 : {}", searchBoard);
        return  searchBoard.map(BoardDTO::toBoardDTO);
    }


    // 게시글 상세 정보
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<BoardDTO> getBoard(Long boardId) {
        // 게시글 조회
        BoardEntity findBoard = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        log.info("게시글 : " + findBoard);
        BoardDTO boardDTO = BoardDTO.toBoardDTO(findBoard);
        return ResponseEntity.ok().body(boardDTO);
    }
}