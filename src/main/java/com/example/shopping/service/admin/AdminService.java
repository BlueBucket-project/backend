package com.example.shopping.service.admin;

import com.example.shopping.domain.Item.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface AdminService {
    // 상품 삭제
    String removeItem(Long itemId, UserDetails userDetails);
    // 게시글 삭제
    String removeBoard(Long boardId, UserDetails userDetails);
    // 상품 관리
    Page<ItemDTO> superitendItemForReserved(Pageable pageable, UserDetails userDetails);
    Page<ItemDTO> superitendItemForSoldOut(Pageable pageable, UserDetails userDetails);
}
