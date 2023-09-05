package com.example.shopping.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 스프링 시큐리티에서 제공하는 로그인 페이지를 안쓰는 설정
                .httpBasic()
                    .disable()
                // JWT 방식을 사용하려면 프론트엔드가 분리된 환경을 가정해야 한다.
                .csrf()
                    .disable()
                .formLogin()
                    .disable()
                .logout()
                    .disable()
                // JWT 방식은 세션저장을 사용하지 않기 때문에 꺼줍니다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                    .antMatchers("/api/v1/boards/write")
                        .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/boards/modify")
                        .access("hasRole('ROLE_USER')")
                    .antMatchers("/api/v1/boards/remove")
                        .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/admin/**")
                        .access("hasRole('ROLE_ADMIN')")
                    .antMatchers("/swagger-resources/**").permitAll()
                    .antMatchers("/swagger-ui/**").permitAll()
                    .antMatchers("/api/v1/users/**").permitAll();


        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode,encoders);
    }
}
