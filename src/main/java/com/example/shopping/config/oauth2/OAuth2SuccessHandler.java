package com.example.shopping.config.oauth2;

import com.example.shopping.domain.jwt.TokenDTO;
import com.example.shopping.domain.member.ResponseMemberDTO;
import com.example.shopping.entity.jwt.TokenEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.jwt.TokenRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
 *          소셜 로그인을 성공하면 프론트에게 필요한 정보들을 JSON으로 보내줍니다.
 *   date : 2023/11/28
 * */
@Log4j2
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    // Jackson ObjectMapper를 주입합니다.
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            log.info("OAuth2 Login 성공!");
            // 소셜 로그인 이메일 가져오기
            String email = authentication.getName();
            log.info("email : " + email);
            // 토큰 조회
            TokenEntity findToken = tokenRepository.findByMemberEmail(email);
            log.info("token : " + findToken);
            // 토큰 DTO 반환
            TokenDTO tokenDTO = TokenDTO.toTokenDTO(findToken);
            // 회원 조회
            MemberEntity findUser = memberRepository.findByEmail(email);
            // 회원 DTO 반환
            ResponseMemberDTO memberDTO = ResponseMemberDTO.socialMember(findUser);

            // 헤더에 담아준다.
            response.addHeader("email", memberDTO.getEmail());

            // 바디에 담아준다.
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("providerId", memberDTO.getProviderId());
            responseBody.put("provider", memberDTO.getProvider());
            responseBody.put("accessToken", tokenDTO.getAccessToken());
            responseBody.put("refreshToken", tokenDTO.getRefreshToken());
            responseBody.put("email", tokenDTO.getMemberEmail());
            responseBody.put("memberId", tokenDTO.getMemberId());
            responseBody.put("grantType", tokenDTO.getGrantType());

            // JSON 응답 전송
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        } catch (Exception e) {
            // 예외가 발생하면 클라이언트에게 오류 응답을 반환
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("정보를 찾아오지 못했습니다.");
            response.getWriter().write("OAuth 2.0 로그인 성공 후 오류 발생: " + e.getMessage());
            // 이 메서드는 버퍼에 있는 내용을 클라이언트에게 보냅니다.
            // 데이터를 작성하고 나서는 flush()를 호출하여 실제로 데이터를 클라이언트로 전송합니다.
            response.getWriter().flush();
        }
    }
}
