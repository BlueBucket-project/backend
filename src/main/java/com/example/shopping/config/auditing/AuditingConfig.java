package com.example.shopping.config.auditing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
/*
 *   writer : YuYoHan
 *   work :
 *          Auditing 기능을 사용해서 작성자, 수정자, 작성 시간, 업데이트 시간을 나타내기 위한
 *          역할을 하고 있습니다.
 *   date : 2023/09/25
 * */
@Configuration
// JPA의 Auditing 기능을 활성화
@EnableJpaAuditing
public class AuditingConfig {
    // 등록자와 수정자를 처리해주는 AuditorAware을 빈으로 등록
    // 빈으로 등록하는 이유는 기능을 사용하기 위해서 기능을 추가할 때는
    // 스프링에서는 자동으로 빈으로 등록하지 않기 때문에 별도로 Bean으로 등록해야 한다.
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
