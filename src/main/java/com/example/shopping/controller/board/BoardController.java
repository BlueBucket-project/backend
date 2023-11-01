package com.example.shopping.controller.board;


import com.example.shopping.domain.board.CreateBoardDTO;
import com.example.shopping.service.board.BoardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;


@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/{itemId}/boards")
@Tag(name = "board", description = "상품 문의 API")
public class BoardController {
    private final BoardServiceImpl boardService;

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
    public String removeBoard(@PathVariable Long boardId,
                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("이메일 : " + email);
            String result = boardService.removeBoard(boardId, email);
            return result;
        } catch (Exception e) {
            return "문의를 삭제하는데 실패했습니다.";
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
        try {
            String email = userDetails.getUsername();
            log.info("이메일 : " + email);
            ResponseEntity<?> responseEntity = boardService.updateBoard(boardId, modifyDTO, email);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 문의 상세 보기
    @GetMapping("/{boardId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "board")
    @Operation(summary = "문의 상세 보기", description = "상품에 대한 문의를 상세히 봅니다.")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            ResponseEntity<?> board = boardService.getBoard(boardId, email);
            return ResponseEntity.ok().body(board);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
