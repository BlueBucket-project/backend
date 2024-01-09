package com.example.shopping.service.board;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.CreateBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

/*
 *   writer : 유요한
 *   work :
 *          게시글 서비스
 *          이렇게 인터페이스를 만들고 상속해주는 방식을 선택한 이유는
 *          메소드에 의존하지 않고 필요한 기능만 사용할 수 있게 하고 가독성과 유지보수성을 높이기 위해서 입니다.
 *   date : 2023/11/28
 * */
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
    String removeBoard(Long boardId, UserDetails userDetails);

    // 문의 자세히 보기
    ResponseEntity<?> getBoard(Long boardId, String memberEmail);

    // 작성자의 문의글 보기
    Page<BoardDTO> getMyBoards(String memberEmail, Pageable pageable, String searchKeyword);

    // 상품에 대한 문의글 보기
    Page<BoardDTO> getBoards(Pageable pageable, Long itemId, String email);
}
