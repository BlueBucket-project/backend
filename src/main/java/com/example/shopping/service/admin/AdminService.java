package com.example.shopping.service.admin;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.board.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface AdminService {
    // 상품 삭제
    String removeItem(Long itemId, UserDetails userDetails);
    // 게시글 삭제
    String removeBoard(Long boardId, UserDetails userDetails);
    // 상품 관리
//    Page<ItemDTO> superitendItem(Pageable pageable, UserDetails userDetails, ItemSellStatus itemSellStatus);
//    // 상품 보기
//    ResponseEntity<ItemDTO> getItem(Long itemId, UserDetails userDetails);
    // 문의글 전체 보기
    Page<BoardDTO> getAllBoards(Pageable pageable, String searchKeyword, UserDetails userDetails);
    // 작성자의 문의글 보기
    Page<BoardDTO> getBoardsByNiickName(UserDetails userDetails, Pageable pageable, String nickName, String searchKeyword);
    // 문의글 상세 페이지 보기
    ResponseEntity<BoardDTO> getBoard(Long boardId, UserDetails userDetails);
}
