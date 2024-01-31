package com.example.shopping.exception;

import com.example.shopping.exception.board.BoardException;
import com.example.shopping.exception.cart.CartException;
import com.example.shopping.exception.comment.CommentException;
import com.example.shopping.exception.externalService.ExternalServiceException;
import com.example.shopping.exception.file.FileDownloadException;
import com.example.shopping.exception.file.FileUploadException;
import com.example.shopping.exception.item.ItemException;
import com.example.shopping.exception.member.UserException;
import com.example.shopping.exception.order.OrderException;
import com.example.shopping.exception.service.OutOfStockException;
import com.example.shopping.exception.sessionExpire.SessionExpiredException;
import com.example.shopping.exception.validation.DataValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/*
 *   writer : 유요한
 *   work :
 *          전역으로 발생한 예외를 처리해줄 수 있는 Class를 생성
 *   date : 2024/01/30
 * */
@RestControllerAdvice
public class GlobalExceptionAdvice {
    // 전체적인 예외처리
    // 이거를 따로 두는 이유는 신경 못쓰는예외가 발생할 것을 대비
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
    }

    // 유저를 못찾을 경우 발생하는 예외처리
    @ExceptionHandler(UserException.class )
    public ResponseEntity<UserException> handleCustomException(UserException userException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userException);
    }

    // 게시글을 못찾을 경우 발생하는 예외처리
    @ExceptionHandler(BoardException.class)
    public ResponseEntity<BoardException> handleCustomException2(BoardException boardException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(boardException);
    }
    // 댓글을 못찾을 경우 발생하는 예외처리
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<CommentException> handleCustomException2(CommentException commentException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commentException);
    }

    // 장바구니 관련 발생하는 예외처리
    @ExceptionHandler(CartException.class)
    public ResponseEntity<CartException> handleCustomException3(CartException cartException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cartException);
    }

    // 주문 관련 발생하는 예외처리
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<OrderException> handleCustomException3(OrderException orderException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(orderException);
    }

    // 상품을 못찾을 경우 발생하는 예외처리
    @ExceptionHandler(ItemException.class)
    public ResponseEntity<ItemException> handleCustomException3(ItemException itemException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(itemException);
    }

    // 검증 오류 예외처리
    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<DataValidationException> handleDataValidationException(DataValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }

    // 인증 예외처리
    @ExceptionHandler(SessionExpiredException.class)
    public ResponseEntity<SessionExpiredException> handleSessionExpiredException(SessionExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex);
    }

    // 파일 업로드 예외처리
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<FileUploadException> handleFileUploadException(FileUploadException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    }

    // 파일 다운로드 예외처리
    @ExceptionHandler(FileDownloadException.class)
    public ResponseEntity<FileDownloadException> handleFileDownloadException(FileDownloadException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    }

    // 외부 서비스 예외처리
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ExternalServiceException> handleExternalServiceException(ExternalServiceException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex);
    }

    // 서비스 로직 예외처리
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<OutOfStockException> handleOutOfStockException(OutOfStockException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }

}
