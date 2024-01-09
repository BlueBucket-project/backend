package com.example.shopping.exception.board;
/*
 *   writer : 유요한
 *   work :
 *          게시글 예외처리 때 사용
 *   date : 2023/10/12
 * */
public class BoardException extends RuntimeException{

    public BoardException(String message) {
        super(message);
    }
}
