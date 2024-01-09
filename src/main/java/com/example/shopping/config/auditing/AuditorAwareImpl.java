package com.example.shopping.config.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
/*
 *   writer : YuYoHan
 *   work :
 *          Auditing 기능을 사용해서 작성자, 수정자, 작성 시간, 업데이트 시간을 나타내기 위한
 *          역할을 하고 있습니다.
 *   date : 2023/09/25
 * */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 인증된 회원은 SecurityContextHolder에 등록되는데
        // 그것을 불러오는 것이다.
        // 인증을 받지 못하면 ananymous라고 나옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = "";

        if(authentication != null) {
            // 현재 로그인한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정
            email = authentication.getName();
        }
        return Optional.of(email);
    }
}
