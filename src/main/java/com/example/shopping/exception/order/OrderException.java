package com.example.shopping.exception.order;
/*
 *   writer : 유요한
 *   work :
 *          주문 예외처리
 *   date : 2024/01/30
 * */
public class OrderException extends RuntimeException{
    public OrderException(String message) {
        super(message);
    }
}
