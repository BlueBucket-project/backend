package com.example.shopping.service.board;

import com.example.shopping.domain.board.CreateBoardDTO;
import org.springframework.http.ResponseEntity;


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
}
