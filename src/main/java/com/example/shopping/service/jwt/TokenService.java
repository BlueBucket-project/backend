package com.example.shopping.service.jwt;

import org.springframework.http.ResponseEntity;

public interface TokenService {
    // accessToken 발급
    ResponseEntity<?> createAccessToken(String refreshToken);
}
