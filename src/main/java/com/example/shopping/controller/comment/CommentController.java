package com.example.shopping.controller.comment;

import com.example.shopping.domain.comment.UpdateCommentDTO;
import com.example.shopping.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


/*
 *   writer : YuYoHan
 *   work :
 *          댓글 작성, 삭제, 수정하는 기능입니다.
 *   date : 2024/01/22
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/{itemId}/boards/{boardId}/comments")
@Log4j2
@Tag(name = "comment", description = "댓글 API")
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "comment")
    @Operation(summary = "댓글 등록", description = "댓글을 등록하는 API입니다.")
    public ResponseEntity<?> saveComment(@PathVariable Long boardId,
                                         @RequestBody UpdateCommentDTO commentDTO,
                                         @AuthenticationPrincipal UserDetails userDetails) {
       try {
           String email = userDetails.getUsername();
           log.info("email : " + email);
           ResponseEntity<?> responseComment = commentService.save(boardId, commentDTO, email);
           return ResponseEntity.ok().body(responseComment);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "comment")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제하는 API입니다.")
    public ResponseEntity<?> removeComment(@PathVariable Long boardId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String responseComment = commentService.remove(boardId, commentId, userDetails);
            return ResponseEntity.ok().body(responseComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "comment")
    @Operation(summary = "댓글 수정", description = "댓글을 수정하는 API입니다.")
    public ResponseEntity<?> updateComment(@PathVariable Long boardId,
                                           @PathVariable Long commentId,
                                           @RequestBody UpdateCommentDTO commentDTO,
                                           @AuthenticationPrincipal UserDetails userDetails) {
       try {
           String email = userDetails.getUsername();
           log.info("email : " + email);
           ResponseEntity<?> update = commentService.update(boardId, commentId, commentDTO, email);
           return update;
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
}
