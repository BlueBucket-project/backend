package com.example.shopping.exception.sessionExpire;
/*
 *   writer : 유요한
 *   work :
 *          인증 예외처리
 *   date : 2023/10/12
 * */
public class SessionExpiredException extends RuntimeException{
    public SessionExpiredException(String message) {
        super(message);
    }
}
