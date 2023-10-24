package com.example.shopping.controller.admin;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderMainDTO;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.service.admin.AdminServiceImpl;
import com.example.shopping.service.item.ItemServiceImpl;
import com.example.shopping.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
@Tag(name = "admin", description = "관리자 API입니다.")
public class AdminController {
    private final AdminServiceImpl adminService;
    private final ItemServiceImpl itemService;

    // 상품 삭제
    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "admin")
    @Operation(summary = "상품 삭제", description = "관리자가 상품을 삭제하는 API입니다.")
    public String removeItem(@PathVariable Long itemId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        String result = adminService.removeItem(itemId, userDetails);
        log.info("result : " + result);
        return result;
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "admin")
    @Operation(summary = "게시글 삭제", description = "관리자가 게시글을 삭제하는 API입니다.")
    public String removeBoard(@PathVariable Long boardId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        String result = adminService.removeBoard(boardId, userDetails);
        log.info("result : " + result);
        return result;
    }

    // 댓글 삭제
    @DeleteMapping("/{boardId}/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "admin")
    @Operation(summary = "댓글 삭제", description = "관리자가 댓글을 삭제하는 API입니다.")
    public String removeComment(@PathVariable Long boardId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        String result = adminService.removeComment(boardId, commentId, userDetails);
        log.info("result : " + result);
        return result;
    }

    // 관리자 페이지
    @GetMapping("")
    @Tag(name = "admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "전체 페이지 보기", description = "관리자가 예약 & 전부 팔린 상품을 보는 API입니다.")
    public ResponseEntity<?> getItems(
            @PageableDefault(sort = "itemId", direction = Sort.Direction.DESC)
            Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("itemSellStatus") ItemSellStatus itemSellStatus) {

        // 파라미터로 상품의 상태를 보고 그거에 맞는 상품을 조회한다.
        // 이렇게 해야 확장성이 늘어난다.
        Page<ItemDTO> items = adminService.superitendItem(pageable, userDetails, itemSellStatus);

        Map<String, Object> pageItem = new HashMap<>();
        // 현재 페이지의 아이템 목록
        pageItem.put("items", items.getContent());
        // 현재 페이지 번호
        pageItem.put("nowPageNumber", items.getNumber());
        // 전체 페이지 수
        pageItem.put("totalPage", items.getTotalPages());
        // 한 페이지에 출력되는 데이터 개수
        pageItem.put("pageSize", items.getSize());
        // 다음 페이지 존재 여부
        pageItem.put("hasNextPage", items.hasNext());
        // 이전 페이지 존재 여부
        pageItem.put("hasPreviousPage", items.hasPrevious());
        // 첫 번째 페이지 여부
        pageItem.put("isFirstPage", items.isFirst());
        // 마지막 페이지 여부
        pageItem.put("isLastPage", items.isLast());

        return ResponseEntity.ok().body(pageItem);
    }

    // 상품 상세 정보
    @GetMapping("/{itemId}")
    @Tag(name = "admin")
    @Operation(summary = "상품 상세 정보 보기", description = "관리자가 상품의 상세정보를 볼 수 있습니다.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> itemDetail(@PathVariable Long itemId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ResponseEntity<ItemDTO> item = adminService.getItem(itemId, userDetails);
            log.info("item : " + item);
            return ResponseEntity.ok().body(item);
        } catch (EntityNotFoundException e) {
            log.error("존재하지 않는 상품입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 상품입니다.");
        }
    }


    @PostMapping(value = "/orderItem")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "상품주문", description = "관리자가 상품을 최종 구매확정하는 API입니다.")
    public ResponseEntity<?> order(@RequestBody List<OrderMainDTO> orders, BindingResult result
            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        OrderDTO orderItem;

        try {
            if (result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            orderItem = adminService.orderItem(orders, email);
            //test데이터 - 배포시 삭제
            //orderItem = orderService.orderItem(orders, "admin123@test.com");

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(orderItem);
    }

    // 관리자 기준 상품 조건으로 보기
    @GetMapping("/search")
    @Tag(name = "admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> searchItemsConditions(@PageableDefault(sort = "itemId", direction = Sort.Direction.DESC)
                                                   Pageable pageable,
                                                   @RequestParam(required = false) String itemName,
                                                   @RequestParam(required = false) String itemPlace,
                                                   @RequestParam(required = false) String itemReserver,
                                                   @RequestParam(required = false) ItemSellStatus itemSellStatus
    ){

        Page<ItemDTO> items = null;

        try{
            items = itemService.searchItemsConditions(pageable, itemName, null, null, null, itemPlace, itemReserver, itemSellStatus);
            Map<String, Object> response = new HashMap<>();
            // 현재 페이지의 아이템 목록
            response.put("items", items.getContent());
            // 현재 페이지 번호
            response.put("nowPageNumber", items.getNumber());
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
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
