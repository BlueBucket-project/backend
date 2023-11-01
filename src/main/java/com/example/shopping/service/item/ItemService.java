package com.example.shopping.service.item;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.UpdateItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    // 상품 등록
    ItemDTO saveItem(ItemDTO itemDTO,
                     List<MultipartFile> itemFiles,
                     String memberEmail) throws Exception;

    // 상품 상세 정보
    ItemDTO getItem(Long itemId, Pageable pageable, String memberEmail);

    // 상품 수정
    ItemDTO updateItem(Long itemId,
                       UpdateItemDTO itemDTO,
                       List<MultipartFile> itemFiles,
                       String memberEmail) throws Exception;

    // 상품 삭제
    String removeItem(Long itemId, String memberEmail);

    // 이미지 삭제
    String removeImg(Long itemId, Long itemImgId, String memberEmail);

    // 전체 상품 보기
    Page<ItemDTO> getItems(Pageable pageable);

    // 검색
    Page<ItemDTO> getSearchItems(Pageable pageable,
                                 String searchKeyword);

}
