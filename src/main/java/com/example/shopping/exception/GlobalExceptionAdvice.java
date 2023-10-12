package com.example.shopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 전역으로 발생한 예외를 처리해줄 수 있는 Class를 생성
@RestControllerAdvice
public class GlobalExceptionAdvice {
    // 전체적인 예외처리
    // 이거를 따로 두는 이유는 신경 못쓰는예외가 발생할 것을 대비
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
    }

    // 유저를 못찾을 경우 발생하는 예외처리
    @ExceptionHandler(UserNotFoundException.class )
    public ResponseEntity<UserException> handleCustomException(UserException userException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userException);
    }

    // 게시글을 못찾을 경우 발생하는 예외처리
    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<BoardException> handleCustomException2(BoardException boardException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(boardException);
    }

    // 상품을 못찾을 경우 발생하는 예외처리
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ItemException> handleCustomException3(ItemException itemException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(itemException);
    }
}
