package com.example.shopping.service.board;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.CreateBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;


public interface BoardService {
    // 문의 등록
    ResponseEntity<?> saveBoard(Long itemId,
                                CreateBoardDTO boardDTO,
                                String memberEmail) throws Exception;

    // 문의 수정
    ResponseEntity<?> updateBoard(Long boardId,
                                  CreateBoardDTO boardDTO,
                                  String memberEmail);

    // 문의 삭제
    String removeBoard(Long boardId, String memberEmail);

    // 문의 자세히 보기
    ResponseEntity<?> getBoard(Long boardId, String memberEmail);

    // 작성자의 문의글 보기
    Page<BoardDTO> getMyBoards(String memberEmail, Pageable pageable, String searchKeyword);

    // 상품에 대한 문의글 보기
    Page<BoardDTO> getBoards(Pageable pageable, Long itemId, String email);
}
