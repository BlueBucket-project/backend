package com.example.shopping.controller.admin;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSearchCondition;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.admin.CodeDTO;
import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.member.RequestMemberDTO;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderMainDTO;
import com.example.shopping.service.admin.AdminServiceImpl;
import com.example.shopping.service.board.BoardService;
import com.example.shopping.service.item.ItemService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *   writer : YuYoHan, 오현진
 *   work :
 *          관리자 기능입니다.
 *          관리자가 상품을 삭제, 게시글 삭제, 모든 문의글 볼 수 있고
 *          상품에 대한 문의글, 회원 문의글을 볼 수 있고 예약된 상품을
 *          구매확정시켜 줍니다. 그리고 상품을 조건에 따라 검색할 수 있습니다.
 *   date : 2024/01/10
 * */

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
@Tag(name = "admin", description = "관리자 API입니다.")
public class AdminController {
    private final AdminServiceImpl adminService;
    private final ItemService itemService;
    private final BoardService boardService;


    // 게시글 삭제
    @DeleteMapping("/boards/{boardId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "admin")
    @Operation(summary = "게시글 삭제", description = "관리자가 게시글을 삭제하는 API입니다.")
    public ResponseEntity<?> removeBoard(@PathVariable Long boardId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String result = boardService.removeBoard(boardId, userDetails);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

            Map<String, Object> response = pageInfo(boards);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private static Map<String, Object> pageInfo(Page<BoardDTO> boards) {
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
        return response;
    }

    // 상품에 대한 문의글 보기
    @GetMapping("/{itemId}/boards")
    @Tag(name = "admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "상품의 전체 문의글 보기", description = "상품의 모든 문의글을 보여주는 API입니다.")
    public ResponseEntity<?> getItemBoards(
            @PageableDefault(sort = "boardId", direction = Sort.Direction.DESC)
            Pageable pageable,
            @PathVariable Long itemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Page<BoardDTO> itemBoards = adminService.getItemBoards(pageable, itemId, userDetails);
            Map<String, Object> response = pageInfo(itemBoards);

            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
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
            Map<String, Object> response = pageInfo(boards);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 문의글 확인합니다.
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

    // 관리자가 상품을 구매확정하는 API입니다.
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
    public ResponseEntity<?> searchItemsConditions(Pageable pageable,
                                                   ItemSearchCondition condition
    ){

        Page<ItemDTO> items = null;

        try{
            items = itemService.searchItemsConditions(pageable, condition);
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

    // 관리자 회원가입
    @PostMapping("")
    @Tag(name = "admin")
    @Operation(summary = "관리자 회원가입", description = "관리자 회원가입 API")
    public ResponseEntity<?> joinAdmin(@Validated @RequestBody RequestMemberDTO admin,
                                       BindingResult result) {
        try {
            // 입력값 검증 예외가 발생하면 예외 메시지를 응답한다.
            if (result.hasErrors()) {
                log.info("BindingResult error : " + result.hasErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getClass().getSimpleName());
            }

            ResponseEntity<?> adminSignUp = adminService.adminSignUp(admin);
            log.info("결과 : " + adminSignUp);
            return ResponseEntity.ok().body(adminSignUp);
        }catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // 관리자 회원가입시 이메일 인증
    @PostMapping("/mails")
    @Tag(name = "admin")
    @Operation(summary = "관리자 이메일 인증 신청", description = "관리자 이메일 인증 신청 API")
    public String emailAuthentication(@RequestParam("email") String email) {
        try {
            String code = adminService.sendMail(email);
            log.info("사용자에게 발송한 인증코드 ==> " + code);
            return "이메일 인증코드가 발급완료";
        } catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return e.getMessage();
        }
    }
    // 인증 코드 검증
    @PostMapping("/verifications")
    @Tag(name = "admin")
    @Operation(summary = "관리자 이메일 인증 검증", description = "관리자 이메일 인증 검증 API")
    public ResponseEntity<?> verificationEmail(@RequestBody CodeDTO code) {
        log.info("코드 : " + code);
        String result = adminService.verifyCode(code.getCode());
        log.info("result : " + result);
        return ResponseEntity.ok().body(result);
    }
}
