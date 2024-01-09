package com.example.shopping.config.oauth2;

import com.example.shopping.config.PrincipalDetails;
import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.config.oauth2.provider.GoogleUser;
import com.example.shopping.config.oauth2.provider.NaverUser;
import com.example.shopping.config.oauth2.provider.OAuth2UserInfo;
import com.example.shopping.domain.jwt.TokenDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.jwt.TokenEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.jwt.TokenRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
/*
 *   writer : YuYoHan
 *   work :
 *          소셜 로그인을 하면 사용자 정보를 서버에서 가져옵니다.
 *          가져온 정보로 회원가입을 시켜주고 JWT을 생성해줍니다.
 *   date : 2023/12/04
 * */
@Service
@Log4j2
@RequiredArgsConstructor
public class PrincipalOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // userRequest.getClientRegistration()은 인증 및 인가된 사용자 정보를 가져오는
        // Spring Security에서 제공하는 메서드입니다.
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        log.info("clientRegistration : " + clientRegistration);
        // 소셜 로그인 accessToken
        String socialAccessToken = userRequest.getAccessToken().getTokenValue();
        log.info("소셜 로그인 accessToken : " + socialAccessToken);

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService =
                new DefaultOAuth2UserService();
        log.info("oAuth2UserService : " + oAuth2UserService);

        // 소셜 로그인한 유저정보를 가져온다.
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        log.info("oAuth2User : " + oAuth2User);
        log.info("getAttribute : " + oAuth2User.getAttributes());

        // 회원가입 강제 진행
        OAuth2UserInfo oAuth2UserInfo = null;
        String registrationId = clientRegistration.getRegistrationId();
        log.info("registrationId : " + registrationId);

        // 구글로 로그인할경우
        if(registrationId.equals("google")) {
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUser(oAuth2User, clientRegistration);
            // 네이버로 로그인할 경우
        } else  if(registrationId.equals("naver")) {
            log.info("네이버 로그인");
            oAuth2UserInfo = new NaverUser(oAuth2User, clientRegistration);
        } else {
            log.error("지원하지 않는 소셜 로그인입니다.");
        }

        // 사용자가 로그인한 소셜 서비스를 가지고 옵니다.
        // 예시) google or naver 같은 값을 가질 수 있다.
        String provider = oAuth2UserInfo.getProvider();
        // 사용자의 소셜 서비스(provider)에서 발급된 고유한 식별자를 가져옵니다.
        // 이 값은 해당 소셜 서비스에서 유니크한 사용자를 식별하는 용도로 사용됩니다.
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        // 사용자의 이메일 주소를 가지고 옵니다.
        // 소셜 서비스에서 제공하는 이메일 정보를 사용합니다.
        String email = oAuth2UserInfo.getEmail();
        // 소셜 로그인의 경우 무조건 USER 등급으로 고정이다.
        Role role = Role.USER;

        MemberEntity findUser = memberRepository.findByEmail(email);

        if(findUser == null) {
            log.info("소셜 로그인이 최초입니다.");
            log.info("소셜 로그인 자동 회원가입을 진행합니다.");

            findUser = MemberEntity.builder()
                    .email(email)
                    .memberName(name)
                    .provider(provider)
                    .providerId(providerId)
                    .memberRole(role)
                    .nickName(name)
                    .build();

            log.info("member : " + findUser);
             findUser = memberRepository.save(findUser);
        } else {
            log.info("로그인을 이미 한적이 있습니다.");
        }
        // 권한 가져오기
        List<GrantedAuthority> authorities = getAuthoritiesForUser(findUser);
        // 토큰 생성
        TokenDTO tokenForOAuth2 = jwtProvider.createTokenForOAuth2(email, authorities, findUser.getMemberId());
        // 기존에 이 토큰이 있는지 확인
        TokenEntity findToken = tokenRepository.findByMemberEmail(tokenForOAuth2.getMemberEmail());

        TokenEntity saveToken;
        // 기존의 토큰이 없다면 새로 만들어준다.
        if(findToken == null) {
            TokenEntity tokenEntity = TokenEntity.tokenEntity(tokenForOAuth2);
            saveToken = tokenRepository.save(tokenEntity);
            log.info("token : " + saveToken);
        } else {
            // 기존의 토큰이 있다면 업데이트 해준다.
            tokenForOAuth2 = TokenDTO.builder()
                    .grantType(tokenForOAuth2.getGrantType())
                    .accessToken(tokenForOAuth2.getAccessToken())
                    .accessTokenTime(tokenForOAuth2.getAccessTokenTime())
                    .refreshToken(tokenForOAuth2.getRefreshToken())
                    .refreshTokenTime(tokenForOAuth2.getRefreshTokenTime())
                    .memberEmail(tokenForOAuth2.getMemberEmail())
                    .memberId(tokenForOAuth2.getMemberId())
                    .build();
            TokenEntity tokenEntity = TokenEntity.updateToken(findToken.getId(), tokenForOAuth2);
            saveToken = tokenRepository.save(tokenEntity);
            log.info("token : " + saveToken);
        }

        // 토큰이 제대로 되어 있나 검증
        // 여기서 이렇게 하는 이유는 OAuth2SuccessHandler에서 인증된 이메일을 뽑아오기 위해서입니다.
        if(StringUtils.hasText(saveToken.getAccessToken())
                && jwtProvider.validateToken(saveToken.getAccessToken())) {
            // 토큰에 인증절차를 해줍니다.
            // 그러면 토큰에 인증과 권한이 주어지게 됩니다.
            Authentication authenticationToken = jwtProvider.getAuthentication(saveToken.getAccessToken());
            log.info("authentication : " + authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 유저에 대한 권한을 주기위해서 구현한 로직입니다.
            UserDetails userDetails = new User(email, "", authorities);
            log.info("userDetails : " + userDetails);
            // UserDetails 객체는 사용자의 주요 정보를 캡슐화하고
            // Authentication 객체는 사용자의 인증 상태와 권한을 나타냅니다
            Authentication authenticationUser =
                    new UsernamePasswordAuthenticationToken(userDetails, authorities);
            log.info("authentication1 : " + authenticationUser);
            SecurityContextHolder.getContext().setAuthentication(authenticationUser);
        } else {
            log.info("검증 실패");
        }
        // attributes가 있는 생성자를 사용하여 PrincipalDetails 객체 생성
        // 소셜 로그인인 경우에는 attributes도 함께 가지고 있는 PrincipalDetails 객체를 생성하게 됩니다.
        PrincipalDetails principalDetails = new PrincipalDetails(findUser, oAuth2User.getAttributes());
        log.info("principalDetails : " + principalDetails);
        return principalDetails;
    }

    // 권한 가져오기 로직
    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity findUser) {
        Role role = findUser.getMemberRole();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        log.info("권한 : " + role.name());
        return authorities;
    }
}
