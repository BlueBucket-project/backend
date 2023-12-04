package com.example.shopping.controller.admin;

import com.example.shopping.config.jwt.JwtAuthenticationEntryPoint;
import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderMainDTO;
import com.example.shopping.service.admin.AdminServiceImpl;
import com.example.shopping.service.board.BoardServiceImpl;
import com.example.shopping.service.item.ItemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    @DeleteMapping("/items/{itemId}")
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
    @DeleteMapping("/boards/{boardId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "admin")
    @Operation(summary = "게시글 삭제", description = "관리자가 게시글을 삭제하는 API입니다.")
    public String removeBoard(@PathVariable Long boardId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        String result = adminService.removeBoard(boardId, userDetails);
        log.info("result : " + result);
        return result;
    }

    // 모든 문의글 보기
    @GetMapping("/boards")
    @Tag(name = "admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "전체 문의글 보기", description = "모든 문의글을 보여주는 API입니다.")
    public ResponseEntity<?> getBoards(
            // SecuritConfig에 Page 설정을 한 페이지에 10개 보여주도록
            // 설정을 해서 여기서는 할 필요가 없다.
            @PageableDefault(sort = "boardId", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // 검색하지 않을 때는 모든 글을 보여준다.
            Page<BoardDTO> boards = adminService.getAllBoards(pageable, searchKeyword, userDetails);

            Map<String, Object> response = new HashMap<>();
            // 현재 페이지의 아이템 목록
            response.put("items", boards.getContent());
            // 현재 페이지 번호
            response.put("nowPageNumber", boards.getNumber() + 1);
            // 전체 페이지 수
            response.put("totalPage", boards.getTotalPages());
            // 한 페이지에 출력되는 데이터 개수
            response.put("pageSize", boards.getSize());
            // 다음 페이지 존재 여부
            response.put("hasNextPage", boards.hasNext());
            // 이전 페이지 존재 여부
            response.put("hasPreviousPage", boards.hasPrevious());
            // 첫 번째 페이지 여부
            response.put("isFirstPage", boards.isFirst());
            // 마지막 페이지 여부
            response.put("isLastPage", boards.isLast());

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 회원 문의글 보기
    @GetMapping("/boards/{nickName}")
    @Tag(name = "admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "전체 문의글 보기", description = "모든 문의글을 보여주는 API입니다.")
    public ResponseEntity<?> getUserBoards(
            // SecuritConfig에 Page 설정을 한 페이지에 10개 보여주도록
            // 설정을 해서 여기서는 할 필요가 없다.
            @PageableDefault(sort = "boardId", direction = Sort.Direction.DESC)
            Pageable pageable,
            String searchKeyword,
            @PathVariable(name = "nickName") String nickName,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // 검색하지 않을 때는 모든 글을 보여준다.
            Page<BoardDTO> boards = adminService.getBoardsByNiickName(userDetails,pageable,nickName,searchKeyword);
            Map<String, Object> response = new HashMap<>();
            // 현재 페이지의 아이템 목록
            response.put("items", boards.getContent());
            // 현재 페이지 번호
            response.put("nowPageNumber", boards.getNumber()+1);
            // 전체 페이지 수
            response.put("totalPage", boards.getTotalPages());
            // 한 페이지에 출력되는 데이터 개수
            response.put("pageSize", boards.getSize());
            // 다음 페이지 존재 여부
            response.put("hasNextPage", boards.hasNext());
            // 이전 페이지 존재 여부
            response.put("hasPreviousPage", boards.hasPrevious());
            // 첫 번째 페이지 여부
            response.put("isFirstPage", boards.isFirst());
            // 마지막 페이지 여부
            response.put("isLastPage", boards.isLast());

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{boardId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "admin")
    @Operation(summary = "문의글 상세 보기", description = "상품에 대한 문의를 상세히 봅니다.")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            ResponseEntity<?> board = adminService.getBoard(boardId, userDetails);
            return ResponseEntity.ok().body(board);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/orderItem")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "admin")
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
    @Operation(summary = "상품 전체 보기(조건)", description = "상품을 조건대로 전체 볼 수 있는 API")
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
            response.put("nowPageNumber", items.getNumber()+1);
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
