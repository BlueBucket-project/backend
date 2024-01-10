package com.example.shopping.service.admin;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.member.RequestMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
/*
 *   writer : 유요한
 *   work :
 *          관리자 서비스
 *          이렇게 인터페이스를 만들고 상속해주는 방식을 선택한 이유는
 *          메소드에 의존하지 않고 필요한 기능만 사용할 수 있게 하고 가독성과 유지보수성을 높이기 위해서 입니다.
 *   date : 2024/01/10
 * */
public interface AdminService {
    // 상품 삭제
    String removeItem(Long itemId, UserDetails userDetails);
    // 게시글 삭제
    String removeBoard(Long boardId, UserDetails userDetails);
    // 문의글 전체 보기
    Page<BoardDTO> getAllBoards(Pageable pageable, String searchKeyword, UserDetails userDetails);
    // 작성자의 문의글 보기
    Page<BoardDTO> getBoardsByNiickName(UserDetails userDetails, Pageable pageable, String nickName, String searchKeyword);
    // 문의글 상세 페이지 보기
    ResponseEntity<BoardDTO> getBoard(Long boardId, UserDetails userDetails);
    // 관리자가 상품의 문의글 보기
    Page<BoardDTO> getItemBoards(Pageable pageable,
                                 Long itemId,
                                 UserDetails userDetails);

    // 관리자 회원가입
    ResponseEntity<?> adminSignUp(RequestMemberDTO admin);
    // 메일 발송
    String sendMail(String email) throws Exception;
    // 검증
    String verifyCode(String code);
}
