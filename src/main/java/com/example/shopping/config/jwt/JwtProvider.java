package com.example.shopping.config.jwt;

import com.example.shopping.domain.jwt.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// PrincipalDetails 정보를 가지고 토큰을 만들어준다.
@Log4j2
@Component
public class JwtProvider {
    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.access.expiration}")
    private long accessTokenTime;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenTime;

    private Key key;

    public JwtProvider(
            @Value("${jwt.secret_key}") String secret_key
    ) {
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secret_key);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 생성
    public TokenDTO createToken(Authentication authentication, List<GrantedAuthority> authorities) {
        // 여기 authentication에서 인증이 완료된 것은 아니다.
        // 프론트에서 header에 accessToken을 담아서 검증을 거쳐야 인증처리가 완료됨
        // 이 시점에서는 아직 실제로 인증이 이루어지지 않았기 때문에 Authenticated 속성은 false로 설정
        // 인증 과정은 AuthenticationManager와 AuthenticationProvider에서 이루어지며,
        // 인증이 성공하면 Authentication 객체의 isAuthenticated() 속성이 true로 변경됩니다.
        log.info("authentication in JwtProvider : " + authentication);
        // role in JwtProvider : ROLE_USER
        log.info("memberRole in JwtProvider : " + authorities);

        // 권한 가져오기
        // authentication객체에서 권한 정보(GrantedAuthority)를 가져와 문자열로 변환
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        // 클레임에 "sub"라는 key로 등록해줌
        claims.put("sub", authentication.getName());

        // claims in JwtProvider : {auth=[ROLE_USER]}
        log.info("claims in JwtProvider : " + claims);
        // authentication.getName() in JwtProvider : zxzz45@naver.com
        log.info("authentication.getName() in JwtProvider : " + authentication.getName());

        // JWT 시간 설정
        long now = (new Date()).getTime();
        Date now2 = new Date();

        // AccessToken 생성
        // 토큰의 만료시간
        Date accessTokenExpire = new Date(now + this.accessTokenTime);
        String accessToken = Jwts.builder()
                .setIssuedAt(now2)
                .setClaims(claims)
                // 내용 exp : 토큰 만료 시간, 시간은 NumericDate 형식(예: 1480849143370)으로 하며
                // 항상 현재 시간 이후로 설정합니다.
                .setExpiration(accessTokenExpire)
                // 서명 : 비밀값과 함께 해시값을 ES256 방식으로 암호화
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // subject 확인
        log.info(checkToken(accessToken));

        // RefreshToken 생성
        Date refreshTokenExpire = new Date(now + this.refreshTokenTime);
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now2)
                .setExpiration(refreshTokenExpire)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info(checkToken(refreshToken));

        TokenDTO tokenDTO = TokenDTO.builder()
                .grantType("Bearer ")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenTime(accessTokenExpire)
                .refreshTokenTime(refreshTokenExpire)
                .memberEmail(authentication.getName())
                .build();

        log.info("token in JwtProvider : " + tokenDTO);
        return tokenDTO;

    }

    //


    private String checkToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String subject = claims.getSubject();
        return subject;
    }

}
