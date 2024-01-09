package com.example.shopping.exception.cart;
/*
 *   writer : 오현진
 *   work :
 *          장바구니 예외처리 때 사용
 *   date : 2023/10/12
 * */
public class CartException extends RuntimeException {
        public CartException(String message) {
            super(message);
        }

}
