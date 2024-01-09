package com.example.shopping.exception.member;
/*
 *   writer : 유요한
 *   work :
 *          유저 예외처리
 *   date : 2023/10/12
 * */
public class UserException extends RuntimeException{

    public UserException(String message) {
        super(message);
    }
}
