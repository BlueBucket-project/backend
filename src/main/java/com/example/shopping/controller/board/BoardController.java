package com.example.shopping.controller.board;


import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.CreateBoardDTO;
import com.example.shopping.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

/*
 *   writer : YuYoHan
 *   work :
 *          문의글 생성, 삭제, 수정, 페이징 처리한 전체보기와
 *          상세보기 기능입니다.
 *   date : 2023/12/06
 * */
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/{itemId}/boards")
@Tag(name = "board", description = "상품 문의 API")
public class BoardController {
    private final BoardService boardService;

    // 문의 등록
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "board")
    @Operation(summary = "문의 등록", description = "상품에 대한 문의를 등록합니다.")
    public ResponseEntity<?> createBoard(
            @PathVariable Long itemId,
            @RequestBody @Validated CreateBoardDTO board,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (bindingResult.hasErrors()) {
                log.error("bindingResult error : " + bindingResult.hasErrors());
                return ResponseEntity.badRequest().body(bindingResult.getClass().getSimpleName());
            }
            String email = userDetails.getUsername();

            ResponseEntity<?> returnBoard = boardService.saveBoard(itemId, board, email);
            return ResponseEntity.ok().body(returnBoard);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 문의 삭제
    @DeleteMapping("/{boardId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "board")
    @Operation(summary = "문의 삭제", description = "상품에 대한 문의를 삭제합니다.")
    public ResponseEntity<?> removeBoard(@PathVariable Long boardId,
                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String result = boardService.removeBoard(boardId, userDetails);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 문의 수정
    @PutMapping("/{boardId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "board")
    @Operation(summary = "문의 수정", description = "상품에 대한 문의를 수정합니다.")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId,
                                         @RequestBody CreateBoardDTO modifyDTO,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        log.info("이메일 : " + email);
        return boardService.updateBoard(boardId, modifyDTO, email);
    }

    // 문의 상세 보기
    // 상품 안에 있는 문의글은 게시글 형태로 되어 있기 때문에  상세보기로 들어가야 한다.
    // 해당 상세보기 기능은 유저를 가리지 않고 그 상품에 관한 문의글이다.
    @GetMapping("/{boardId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "board")
    @Operation(summary = "문의 상세 보기", description = "상품에 대한 문의를 상세히 봅니다.")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            ResponseEntity<?> board = boardService.getBoard(boardId, email);
            return ResponseEntity.ok().body(board);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 상품에 대한 문의글 전체 보기
    @GetMapping("")
    @Tag(name = "board")
    @Operation(summary = "문의글 전체 보기", description = "모든 상품에 대한 문의글을 봅니다.")
    public ResponseEntity<?> getBoards(
            // SecuritConfig에 Page 설정을 한 페이지에 10개 보여주도록
            // 설정을 해서 여기서는 할 필요가 없다.
            // 여기보다 레포지토리에서 적용한것이 우선이 됩니다.
            @PageableDefault(sort = "boardId", direction = Sort.Direction.DESC)
            Pageable pageable,
            @PathVariable(name = "itemId") Long itemId,
            @RequestParam(value = "email", required = false) String email) {
        try {
            log.info("email : " + email);
            // 검색하지 않을 때는 모든 글을 보여준다.
            Page<BoardDTO> boards = boardService.getBoards(pageable, itemId, email);
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

}
