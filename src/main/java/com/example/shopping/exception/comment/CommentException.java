package com.example.shopping.exception.comment;
/*
 *   writer : 유요한
 *   work :
 *          댓글 예외처리 때 사용
 *   date : 2024/01/18
 * */
public class CommentException extends RuntimeException{

    public CommentException(String message) {
        super(message);
    }
}
