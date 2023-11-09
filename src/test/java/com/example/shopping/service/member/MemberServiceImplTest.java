package com.example.shopping.service.member;

import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.domain.jwt.TokenDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.member.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.aspectj.bridge.MessageUtil.fail;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class MemberServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;


    private MemberEntity createMemberInfo() {
        return MemberEntity.builder()
                .email("zxzz11@naver.com")
                .memberPw(passwordEncoder.encode("zxzz12"))
                .memberName("테스터")
                .memberRole(Role.USER)
                .nickName("테스터")
                .address(AddressEntity.builder()
                        .memberAddr("서울시 xxx")
                        .memberAddrDetail("xxx")
                        .memberZipCode("101-1")
                        .build()).build();
    }
    // 테스트가 실행될 때마다 데이터 베이스를 비워준다.
    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUp() {
        createMemberInfo();
        MemberEntity findUser = memberRepository.findByEmail(createMemberInfo().getEmail());
        if(findUser == null) {
            MemberEntity member = MemberEntity.builder()
                    .email(createMemberInfo().getEmail())
                    .memberPw(createMemberInfo().getMemberPw())
                    .memberName(createMemberInfo().getMemberName())
                    .memberRole(createMemberInfo().getMemberRole())
                    .nickName(createMemberInfo().getNickName())
                    .address(AddressEntity.builder()
                            .memberAddr(createMemberInfo().getAddress().getMemberAddr())
                            .memberAddrDetail(createMemberInfo().getAddress().getMemberAddrDetail())
                            .memberZipCode(createMemberInfo().getAddress().getMemberZipCode())
                            .build()).build();

            MemberEntity save = memberRepository.save(member);
            log.info("회원가입 : " + save);
            Assertions.assertThat(save.getEmail()).isEqualTo(createMemberInfo().getEmail());
            Assertions.assertThat(save.getMemberName()).isEqualTo(createMemberInfo().getMemberName());
            Assertions.assertThat(save.getMemberRole()).isEqualTo(createMemberInfo().getMemberRole());
            Assertions.assertThat(save.getNickName()).isEqualTo(createMemberInfo().getNickName());
            Assertions.assertThat(save.getAddress().getMemberAddr())
                    .isEqualTo(createMemberInfo().getAddress().getMemberAddr());
            Assertions.assertThat(save.getAddress().getMemberAddrDetail())
                    .isEqualTo(createMemberInfo().getAddress().getMemberAddrDetail());
            Assertions.assertThat(save.getAddress().getMemberZipCode())
                    .isEqualTo(createMemberInfo().getAddress().getMemberZipCode());
        } else {
            log.error("이미 중복된 아이디입니다.");
        }
    }

    @Test
    @DisplayName("로그인 기능 테스트")
    void login() {
        MemberEntity memberInfo = createMemberInfo();
        MemberEntity save = memberRepository.save(memberInfo);
        MemberEntity findUser = memberRepository.findByEmail(save.getEmail());

        if(passwordEncoder.matches(memberInfo.getMemberPw(), findUser.getMemberPw())) {
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(findUser.getEmail(), findUser.getMemberPw());
            List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findUser);

            // JWT 생성
            TokenDTO token = jwtProvider.createToken(authentication, authoritiesForUser);
            Assertions.assertThat(token).isNotNull();
        }
    }
    // 회원의 권한을 GrantedAuthority타입으로 반환하는 메소드
    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity member) {
        Role memberRole = member.getMemberRole();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + memberRole.name()));
        log.info("role : " + authorities);
        return authorities;
    }

    @Test
    @DisplayName("조회하는 기능 테스트")
    void search() {
        MemberEntity memberInfo = createMemberInfo();
        MemberEntity save = memberRepository.save(memberInfo);
        MemberEntity findUser = memberRepository.findByEmail(save.getEmail());
        log.info("user : " + findUser);
        Assertions.assertThat(findUser.getEmail()).isEqualTo("zxzz11@naver.com");
    }

    @Test
    @DisplayName("삭제하는 기능 테스트")
    void removeUser() {
        MemberEntity memberInfo = createMemberInfo();
        MemberEntity save = memberRepository.save(memberInfo);

        memberRepository.deleteByMemberId(save.getMemberId());
        MemberEntity checkUser = memberRepository.findByEmail(createMemberInfo().getEmail());
        Assertions.assertThat(checkUser).isNull();

    }


    @Test
    @DisplayName("업데이트 기능 테스트")
    void updateUser() {
        MemberEntity findUser = memberRepository.findByEmail(createMemberInfo().getEmail());

        if(findUser != null) {
            MemberEntity member = MemberEntity.builder()
                    .email("zxzz12@naver.com")
                    .memberPw(passwordEncoder.encode("zxzz12"))
                    .memberName("테스터2")
                    .memberRole(createMemberInfo().getMemberRole())
                    .nickName("테스터2")
                    .address(AddressEntity.builder()
                            .memberAddr(createMemberInfo().getAddress().getMemberAddr())
                            .memberAddrDetail(createMemberInfo().getAddress().getMemberAddrDetail())
                            .memberZipCode(createMemberInfo().getAddress().getMemberZipCode())
                            .build()).build();
            MemberEntity save = memberRepository.save(member);
            Assertions.assertThat(save.getEmail()).isEqualTo(member.getEmail());
            Assertions.assertThat(save.getMemberName()).isEqualTo(member.getMemberName());
            Assertions.assertThat(save.getMemberRole()).isEqualTo(member.getMemberRole());
            Assertions.assertThat(save.getNickName()).isEqualTo(member.getNickName());
            Assertions.assertThat(save.getAddress().getMemberAddr())
                    .isEqualTo(member.getAddress().getMemberAddr());
            Assertions.assertThat(save.getAddress().getMemberAddrDetail())
                    .isEqualTo(member.getAddress().getMemberAddrDetail());
            Assertions.assertThat(save.getAddress().getMemberZipCode())
                    .isEqualTo(member.getAddress().getMemberZipCode());
        }
    }

    @Test
    @DisplayName("이메일 중복 체크")
    void emailCheck() {
        MemberEntity findUser = memberRepository.findByEmail(createMemberInfo().getEmail());
        Assertions.assertThat(findUser).isNull();
    }

    @Test
    @DisplayName("이메일 중복 체크")
    void emailCheck_fail() {
        // given
        MemberEntity memberInfo = createMemberInfo();
        MemberEntity memberInfo2 = createMemberInfo();

        // when
        MemberEntity save = memberRepository.save(memberInfo);

        // then
        Assertions.assertThatThrownBy(() -> memberRepository.save(memberInfo2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복된 이메일 주소입니다.");
        fail("예외가 발생해야 합니다.");
    }

    @Test
    @DisplayName("닉네임 중복 체크")
    void nickNameCheck() {
        MemberEntity findNickName = memberRepository.findByNickName(createMemberInfo().getNickName());
        Assertions.assertThat(findNickName).isNull();
    }

    @Test
    @DisplayName("중복회원 예외")
    void 중복회원_예외() {
        // given
        MemberEntity memberInfo = createMemberInfo();
        MemberEntity memberInfo2 = createMemberInfo();

        // when
        memberRepository.save(memberInfo);

        // then
        // 코드 실행 중 예외가 발생하지 않으면 테스트가 실패합니다.
        Assertions.assertThatThrownBy(() -> memberRepository.save(memberInfo2))
                .isInstanceOf(IllegalStateException.class);
        fail("예외가 발생해야 합니다.");
    }
}