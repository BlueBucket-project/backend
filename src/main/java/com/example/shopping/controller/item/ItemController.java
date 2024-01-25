package com.example.shopping.controller.item;

import com.example.shopping.domain.Item.*;
import com.example.shopping.service.item.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *   writer : YuYoHan, 오현진
 *   work :
 *          상품 작성, 삭제, 수정, 전체 가져오기 그리고 검색하는 기능입니다.
 *   date : 2024/01/23
 * */
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@Tag(name = "item", description = "상품 API")
public class ItemController {
    private final ItemService itemService;

    // 상품 등록
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "item")
    @Operation(summary = "상품 등록", description = "상품을 등록하는 API입니다.")
    public ResponseEntity<?> createItem(
            @Valid @RequestPart("key") CreateItemDTO item,
            @RequestPart(value = "files", required = false) List<MultipartFile> itemFiles,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            if (result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            ItemDTO savedItem = itemService.saveItem(item, itemFiles, email);
            //testData
            //ItemDTO savedItem = itemServiceImpl.saveItem(itemInfo, itemFiles, "mem123@test.com");
            return ResponseEntity.ok().body(savedItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 상품 상세 정보
    @GetMapping("/{itemId}")
    @Tag(name = "item")
    @Operation(summary = "상품 상세 정보 보기", description = "상품의 상세정보를 볼 수 있습니다.")
    public ResponseEntity<?> itemDetail(@PathVariable Long itemId) {
        try {
            ItemDTO item = itemService.getItem(itemId);
            log.info("item : " + item);
            return ResponseEntity.ok().body(item);
        } catch (EntityNotFoundException e) {
            log.error("존재하지 않는 상품입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 상품입니다.");
        }
    }

    // 상품 수정
    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "item")
    @Operation(summary = "상품 수정", description = "상품을 수정하는 API입니다.")
    public ResponseEntity<?> updateItem(@PathVariable Long itemId,
                                        @RequestPart("key") UpdateItemDTO itemDTO,
                                        @RequestPart(value = "files", required = false) List<MultipartFile> itemFiles
            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {

            String email = userDetails.getUsername();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            ItemDTO updateItem = itemService.updateItem(itemId, itemDTO, itemFiles, email, role);

            return ResponseEntity.ok().body(updateItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 상품 삭제
    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "item")
    @Operation(summary = "상품 삭제", description = "상품을 삭제하는 API입니다.")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId
            , @RequestBody DelItemDTO itemDTO
            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String email = userDetails.getUsername();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            String result = itemService.removeItem(itemId, itemDTO.getItemSeller(), email, role);

            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 전체 상품을 볼 수 있고 상품조건 여러개의 경우 조건 조회하기
    // http://localhost:8080/api/v1/items/search?name=당&page=1&sort=itemId,asc&place=종로
    @GetMapping("/search")
    @Tag(name = "item")
    @Operation(summary = "상품 전체", description = "모든 상품을 보여주는 API입니다.")
    public ResponseEntity<?> searchItemsConditions(Pageable pageable,
                                                   ItemSearchCondition condition) {
        Page<ItemDTO> items;
        try {
            log.info("condition : " + condition);
            items = itemService.searchItemsConditions(pageable, condition);
            log.info("상품 조회 {}", items);

            Map<String, Object> response = new HashMap<>();
            // 현재 페이지의 아이템 목록
            response.put("items", items.getContent());
            // 현재 페이지 번호
            response.put("nowPageNumber", items.getNumber() + 1);
            // 전체 페이지 수
            response.put("totalPage", items.getTotalPages());
            // 한 페이지에 출력되는 데이터 개수
            response.put("pageSize", items.getSize());
            // 다음 페이지 존재 여부
            response.put("hasNextPage", items.hasNext());
            // 이전 페이지 존재 여부
            response.put("hasPreviousPage", items.hasPrevious());
            // 첫 번째 페이지 여부
            response.put("isFirstPage", items.isFirst());
            // 마지막 페이지 여부
            response.put("isLastPage", items.isLast());

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 상품의 판매 지역을 반환해줍니다.
    @GetMapping("/sellplace")
    @Tag(name = "item")
    @Operation(summary = "상품 판매지역 리스트", description = "모든 상품의 판매지역을 보여주는 API입니다.")
    public ResponseEntity<?> getSellPlaceList() {

        return ResponseEntity.ok().body(itemService.getSellPlaceList());
    }

}
