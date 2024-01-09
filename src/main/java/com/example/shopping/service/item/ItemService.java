package com.example.shopping.service.item;

import com.example.shopping.domain.Item.CreateItemDTO;
import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSearchCondition;
import com.example.shopping.domain.Item.UpdateItemDTO;
import com.example.shopping.domain.container.ItemContainerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    // 상품 등록
    ItemDTO saveItem(CreateItemDTO itemDTO,
                     List<MultipartFile> itemFiles,
                     String memberEmail) throws Exception;

    // 상품 상세 정보
    ItemDTO getItem(Long itemId);

    // 상품 수정
    ItemDTO updateItem(Long itemId,
                       UpdateItemDTO itemDTO,
                       List<MultipartFile> itemFiles,
                       String memberEmail,
                       String role) throws Exception;

    // 상품 삭제
    String removeItem(Long itemId, Long sellerId, String memberEmail, String role);

    // 검색
    Page<ItemDTO> searchItemsConditions(Pageable pageable, ItemSearchCondition condition);

    // 상품 창고 검색
    List<ItemContainerDTO> getSellPlaceList();
}
