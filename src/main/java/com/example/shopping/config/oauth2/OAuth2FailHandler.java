package com.example.shopping.config.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 *   writer : YuYoHan
 *   work :
 *          소셜 로그인이 실패하면 실패에 대한 메시지를 JSON으로 보내줍니다.
 *   date : 2023/11/28
 * */
@Log4j2
@RequiredArgsConstructor
@Component
public class OAuth2FailHandler implements AuthenticationFailureHandler {
    // Jackson ObjectMapper를 주입합니다.
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        // 실패 시 수행할 로직을 구현
        log.error("OAuth2 로그인 실패: {}", exception.getMessage());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error 발생 : " , exception.getMessage());

        // JSON 응답 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
