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

    // 게시글 수정
    @Override
    public ResponseEntity<?> updateBoard(Long boardId,
                                         CreateBoardDTO boardDTO,
//                                         List<MultipartFile> boardFiles,
                                         String memberEmail) {
        try {
            // 게시글 조회
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("게시글 : " + findBoard);
            // 유저 조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("유저 : " + findUser);
            // 이미지 가져오기
//            List<BoardImgEntity> boardImgEntityList = findBoard.getBoardImgEntityList();
            // 이미지 S3에 올리기
//            List<BoardImgDTO> uploadImg = s3BoardImgUploaderService.upload("board", boardFiles);
            // 댓글 가져오기
            List<CommentEntity> commentEntityList = findBoard.getCommentEntityList();


            // 게시글을 등록한 이메일이 맞다면 true
            if (findUser.getEmail().equals(findBoard.getMember().getEmail())) {
                /*  이미지를 추가할 경우를 대비해서 주석처리 */
//                // 가져온 이미지가 비어있다면
//                if (boardImgEntityList.isEmpty()) {
//                    // s3에 넣어준 이미지들을 풀어준다.
//                    for (int i = 0; i < uploadImg.size(); i++) {
//                        // s3에 올린 이미지들을 하나하나 가져와서 DTO 작업해준다.
//                        BoardImgDTO boardImgDTO = uploadImg.get(i);
//                        // 풀어준 이미지들을 엔티티로 바꿔준다.
//                        BoardImgEntity boardImgEntity = BoardImgEntity.builder()
//                                .uploadImgName(boardImgDTO.getUploadImgName())
//                                .uploadImgPath(boardImgDTO.getUploadImgPath())
//                                .oriImgName(boardImgDTO.getOriImgName())
//                                .uploadImgUrl(boardImgDTO.getUploadImgUrl())
//                                .repImgYn(i == 0 ? "Y" : "N")
//                                .board(findBoard)
//                                .build();
//                        log.info("이미지 : " + boardImgEntity);
//                        // 이미지들을 리스트에 넣어준다.
//                        boardImgEntityList.add(boardImgEntity);
//                    }
//                } else {
//                    // 가져온 이미지가 있다면
//                    // s3에 넣어준 이미지들을 풀어준다.
//                    for (int i = 0; i < uploadImg.size(); i++) {
//                        // s3에 올린 이미지들을 하나하나 가져와서 DTO 작업해준다.
//                        BoardImgDTO boardImgDTO = uploadImg.get(i);
//                        // 풀어준 이미지들을 엔티티로 바꿔준다.
//                        BoardImgEntity boardImgEntity = BoardImgEntity.builder()
//                                .boardImgId(boardImgDTO.getBoardImgId())
//                                .uploadImgName(boardImgDTO.getUploadImgName())
//                                .uploadImgPath(boardImgDTO.getUploadImgPath())
//                                .oriImgName(boardImgDTO.getOriImgName())
//                                .uploadImgUrl(boardImgDTO.getUploadImgUrl())
//                                .repImgYn(i == 0 ? "Y" : "N")
//                                .board(findBoard)
//                                .build();
//                        log.info("이미지 : " + boardImgEntity);
//                        // 이미지들을 리스트에 넣어준다.
//                        boardImgEntityList.add(boardImgEntity);
//                    }
//                }
            }
            for (CommentEntity commentEntity : commentEntityList) {
                // 수정할 내용, 유저정보, 이미지들을 넘겨준다.
                findBoard = BoardEntity.updateBoard(boardDTO, findUser);
                findBoard.getCommentEntityList().add(commentEntity);

            }
            BoardEntity saveBoard = boardRepository.save(findBoard);
            BoardDTO returnBoard = BoardDTO.toBoardDTO(saveBoard);
            return ResponseEntity.ok().body(returnBoard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 이미지 삭제
//    @Override
//    public String removeImg(Long boardId, String memberEmail) {
//        // 이미지 조회
//        BoardImgEntity findImg = boardImgRepository.findById(boardImgId)
//                .orElseThrow(EntityNotFoundException::new);
//        // 유저 조회
//        MemberEntity findUser = memberRepository.findByEmail(memberEmail);
//        // 게시글 조회
//        BoardEntity findBoard = boardRepository.findById(boardId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        String uploadFilePath = findImg.getUploadImgPath();
//        String uuidFileName = findImg.getUploadImgName();
//
//        // 이미지 엔티티에 있는 Item엔티티에 담겨있는 id와 조회한 Item엔티티 id와 일치하면 true
//        if(findImg.getBoard().getBoardId().equals(findBoard.getBoardId())) {
//            // 상품 엔티티에 담아져 있는 유저의 이메일과 조회한 유저의 이메일과 일치하면 true
//            if(findBoard.getMember().getEmail().equals(findUser.getEmail())) {
//                // DB에서 이미지 삭제
//                boardImgRepository.deleteById(findBoard.getBoardId());
//                // S3에서 이미지 삭제
//                String result = s3BoardImgUploaderService.deleteFile(uploadFilePath, uuidFileName);
//                log.info("S3 삭제 결과 : " + result);
//
//                // 위에서 이미지를 삭제하는데 나머지가 있는지 불러온다.
//                List<BoardImgEntity> findBoardImgList = boardImgRepository.findByBoardBoardId(boardId);
//
//                // 이미지를 전부 삭제하지 않았다면
//                if(!findBoardImgList.isEmpty()) {
//                    for (int i = 0; i < findBoardImgList.size(); i++) {
//                        BoardImgEntity boardImgEntity = findBoardImgList.get(i);
//                        BoardImgEntity boardImg = BoardImgEntity.builder()
//                                .boardImgId(boardImgEntity.getBoardImgId())
//                                .uploadImgName(boardImgEntity.getUploadImgName())
//                                .uploadImgPath(boardImgEntity.getUploadImgPath())
//                                .oriImgName(boardImgEntity.getOriImgName())
//                                .uploadImgUrl(boardImgEntity.getUploadImgUrl())
//                                .repImgYn(i == 0 ? "Y" : "N")
//                                .board(findBoard)
//                                .build();
//                        log.info("이미지 : " + boardImgEntity);
//                        findBoardImgList.add(boardImg);
//                    }
//                } else {
//                    // 모든 이미지를 삭제했으므로 상품의 이미지 목록을 비웁니다.
//                    findBoardImgList = new ArrayList<>();
//                }
//                findBoard = BoardEntity.builder()
//                        .title(findBoard.getTitle())
//                        .content(findBoard.getContent())
//                        .member(findBoard.getMember())
//                        .boardImgEntityList(findBoardImgList)
//                        .commentEntityList(findBoard.getCommentEntityList())
//                        .build();
//                boardRepository.save(findBoard);
//                return "삭제완료 했습니다.";
//            }
//            return "삭제를 실패했습니다.";
//        } else {
//            return "삭제를 실패했습니다.";
//        }
//    }
}