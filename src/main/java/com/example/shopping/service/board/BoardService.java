package com.example.shopping.service.board;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.CreateBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    // 게시글 등록
    ResponseEntity<?> saveBoard(CreateBoardDTO boardDTO,
//                                List<MultipartFile> boardFiles,
                                String memberEmail) throws Exception;

    // 게시글 상세정보
    ResponseEntity<BoardDTO> getBoard(Long boardId);

    // 게시글 수정
    ResponseEntity<?> updateBoard(Long boardId,
                                  CreateBoardDTO boardDTO,
//                                  List<MultipartFile> boardFiles,
                                  String memberEmail);

    // 이미지 삭제
//    String removeImg(Long boardId, Long boardImgId, String memberEmail);

    // 게시글 삭제
    String removeBoard(Long boardId, String memberEmail);

    // 전체 게시글 보여주기
    Page<BoardDTO> getBoards(Pageable pageable);

    // 검색
    Page<BoardDTO> getSearchBoards(Pageable pageable, String searchKeyword);


}
