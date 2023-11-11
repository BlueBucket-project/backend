package com.example.shopping.service.jwt;

import com.example.shopping.config.jwt.JwtAuthenticationFilter;
import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.domain.jwt.TokenDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.jwt.TokenEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.jwt.TokenRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<TokenDTO> createAccessToken(String email) {
        // 토큰 조회
        TokenEntity findToken = tokenRepository.findByMemberEmail(email);
        log.info("토큰 : " + findToken);
        // 유저 조회
        MemberEntity findUser = memberRepository.findByEmail(email);
        log.info("유저 : " + findUser);

        // refreshToken 2차 검증
        if (jwtProvider.validateToken(findToken.getRefreshToken())) {
            // 권한 가져오기
            List<GrantedAuthority> authorities = getAuthoritiesForUser(findUser);
            // 토큰 생성
            TokenDTO accessToken = jwtProvider.createAccessToken(findUser.getEmail(), authorities);
            log.info("accessToken : " + accessToken);

            findToken = TokenEntity.builder()
                    .id(findToken.getId())
                    .grantType(accessToken.getGrantType())
                    .accessToken(accessToken.getAccessToken())
                    .accessTokenTime(accessToken.getAccessTokenTime())
                    .memberEmail(accessToken.getMemberEmail())
                    .refreshToken(findToken.getRefreshToken())
                    .refreshTokenTime(findToken.getAccessTokenTime())
                    .build();

            log.info("token : " + findToken);
            TokenEntity saveToken = tokenRepository.save(findToken);
            TokenDTO returnToken = TokenDTO.toTokenDTO(saveToken);

            return new ResponseEntity<>(returnToken, HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("Unexpected token");
        }
    }

    // 주어진 사용자에 대한 권한 정보를 가져오는 로직을 구현하는 메서드입니다.
    // 이 메서드는 데이터베이스나 다른 저장소에서 사용자의 권한 정보를 조회하고,
    // 해당 권한 정보를 List<GrantedAuthority> 형태로 반환합니다.
    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity member) {
        // 예시: 데이터베이스에서 사용자의 권한 정보를 조회하는 로직을 구현
        // member 객체를 이용하여 데이터베이스에서 사용자의 권한 정보를 조회하는 예시로 대체합니다.
        Role role = member.getMemberRole();  // 사용자의 권한 정보를 가져오는 로직 (예시)

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        log.info("role : " + role.name());
        log.info("authorities : " + authorities);
        return authorities;
    }
}
