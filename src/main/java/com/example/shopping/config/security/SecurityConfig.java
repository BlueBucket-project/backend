package com.example.shopping.config.security;

import com.example.shopping.config.jwt.JwtAccessDeniedHandler;
import com.example.shopping.config.jwt.JwtAuthenticationEntryPoint;
import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.config.oauth2.OAuth2FailHandler;
import com.example.shopping.config.oauth2.OAuth2SuccessHandler;
import com.example.shopping.config.oauth2.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

/*
 *   writer : YuYoHan
 *   work :
 *          시큐리티 기능을 사용하기 위한 클래스입니다.
 *   date : 2024/01/10
 * */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
// @EnableGlobalMethodSecurity 어노테이션은 Spring Security에서 메서드 수준의 보안 설정을 활성화하는데
// 사용되는 어노테이션입니다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final PrincipalOAuth2UserService principalOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailHandler oAuth2FailHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CorsFilter라는 필터가 존재하는데 이를 활성화 시키는 작업
                .cors().and()
                // 스프링 시큐리티에서 제공하는 로그인 페이지를 안쓰는 설정
                // httpBasic은 기본적으로 disable이지만 켜두면 위와 같이 알림창이 뜹니다.
                // 쿠키와 세션을 이용한 방식이 아니라 request header에 id와 password값을 직접 날리는 방식이라
                // 보안에 굉장히 취약합니다. REST API에서는 오로지 토큰 방식을 이용하기 때문에 보안에 취약한
                // httpBasic 방식은 해제한다고 보시면 됩니다.
                .httpBasic()
                    .disable()
                // JWT 방식을 사용하려면 프론트엔드가 분리된 환경을 가정해야 한다.
                // 세션을 사용하지 않고 JWT 토큰을 활용하여 진행하고
                // REST API를 만드는 작업이기 때문에 이 처리를 합니다.
                .csrf()
                    .disable()
                // formLogin 기능을 끈다고 해서 form 태그 내에 로그인 기능을 못쓴다는 것은 아닙니다.
                // formLogin을 끄면 초기 로그인 화면이 사라집니다.
                // 그것보다 궁극적인 이유는 아래에 설명할 JWT의 기능을 만들기 위해서 입니다.
                // formLogin은 세션 로그인 방식에서 로그인을 자동처리 해준다는 장점이 존재했는데,
                // JWT에서는 로그인 방식 내에 JWT 토큰을 생성하는 로직이 필요하기 때문에
                // 로그인 과정을 수동으로 클래스를 만들어줘야 하기 때문에 formLogin 기능을 제외 합니다.
                // formLogin 기능 자체가 REST API에 반대되는 특징을 가지고 있습니다.
                // formLogin의 defaultSuccessUrl 메소드로 로그인 성공 시 리다이렉트 할 주소를 입력하게 되는데
                // REST API에서는 서버가 페이지의 기능을 결정하면 안되기 때문에
                // 결과적으로 필요하지 않은 formLogin은 disable합니다.
                .formLogin()
                    .disable()
                .logout()
                    .disable()
                // JWT 방식은 세션저장을 사용하지 않기 때문에 꺼줍니다.
                // 스프링 시큐리티에서 세션을 관리하지 않겠다는 뜻입니다.
                // 서버에서 관리되는 세션없이 클라이언트에서 요청하는 헤더에 token을
                // 담아보낸다면 서버에서 토큰을 확인하여 인증하는 방식을 사용할 것이므로
                // 서버에서 관리되어야할 세션이 필요없습니다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                // 인증절차에 대한 설정
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/api/v1/users")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/users/{memberId}")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/users/**").permitAll()
                .antMatchers("/api/v1/{itemId}/boards/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/v1/boards/{boardId}")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/v1/boards")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/boards/{boardId}")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/{itemId}/boards/{boardId}/comments/**")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/items/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/items")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/v1/items/{itemId}")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/items/{itemId}")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST,"/api/v1/admins").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/admins/mails").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/admins/verifications").permitAll()
                .antMatchers("/api/v1/cart")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/cart/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/v1/cart")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/v1/cart/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT,"/api/v1/cart/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admins/**")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/v1/admins/**")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admins/**")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll();

        // JWT
        http
                // JWT Token을 위한 Filter를 아래에서 만들어 줄건데,
                // 이 Filter를 어느위치에서 사용하겠다고 등록을 해주어야 Filter가 작동이 됩니다.
                .apply(new JwtSecurityConfig(jwtProvider));

        // 에러 방지
        http
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());

        // OAuth2
        http
                // oauth2Login() 메서드는 OAuth 2.0 프로토콜을 사용하여 소셜 로그인을 처리하는 기능을 제공합니다.
                .oauth2Login()
                // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
                .userInfoEndpoint()
                // OAuth2 로그인 성공 시, 후작업을 진행할 서비스
                .userService(principalOAuth2UserService)
                .and()
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailHandler);

        // actuator
        http
                .authorizeRequests()
                // /actuator/** 엔드포인트에 대해 인증이 필요하도록 설정
                .antMatchers("/actuator/**").permitAll();

        return http.build();
    }

    // 확장성과 유연성을 위해서 비밀번호 암호화 설정을
    // 빈으로 등록했습니다.
    @Bean
    PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return p -> {
            p.setOneIndexedParameters(true);    // 1 페이지 부터 시작
            p.setMaxPageSize(10);               // 한 페이지에 10개씩 출력
        };
    }
}
