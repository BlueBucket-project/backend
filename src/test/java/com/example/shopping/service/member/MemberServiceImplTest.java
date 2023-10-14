package com.example.shopping.service.member;

import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.domain.member.AddressDTO;
import com.example.shopping.domain.member.MemberDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.jwt.TokenRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
@ExtendWith(SpringExtension.class)
class MemberServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private TokenRepository tokenRepository;

    private MemberDTO createMemberInfo() {
        return MemberDTO.builder()
                .email("zxzz11@naver.com")
                .memberPw(passwordEncoder.encode("zxzz12"))
                .memberName("테스터")
                .memberRole(Role.USER)
                .nickName("테스터")
                .memberAddress(AddressDTO.builder()
                        .memberAddr("서울시 xxx")
                        .memberAddrDetail("xxx")
                        .memberAddrEtc("101-1")
                        .build()).build();
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
                            .memberAddr(createMemberInfo().getMemberAddress().getMemberAddr())
                            .memberAddrDetail(createMemberInfo().getMemberAddress().getMemberAddrDetail())
                            .memberAddrEtc(createMemberInfo().getMemberAddress().getMemberAddrEtc())
                            .build()).build();

            MemberEntity save = memberRepository.save(member);
            log.info("회원가입 : " + save);
            Assertions.assertThat(save.getEmail()).isEqualTo(createMemberInfo().getEmail());
            Assertions.assertThat(save.getMemberName()).isEqualTo(createMemberInfo().getMemberName());
            Assertions.assertThat(save.getMemberRole()).isEqualTo(createMemberInfo().getMemberRole());
            Assertions.assertThat(save.getNickName()).isEqualTo(createMemberInfo().getNickName());
            Assertions.assertThat(save.getAddress().getMemberAddr())
                    .isEqualTo(createMemberInfo().getMemberAddress().getMemberAddr());
            Assertions.assertThat(save.getAddress().getMemberAddrDetail())
                    .isEqualTo(createMemberInfo().getMemberAddress().getMemberAddrDetail());
            Assertions.assertThat(save.getAddress().getMemberAddrEtc())
                    .isEqualTo(createMemberInfo().getMemberAddress().getMemberAddrEtc());
        } else {
            log.error("이미 중복된 아이디입니다.");
        }
    }

    @Test
    void search() {
        MemberEntity findUser = memberRepository.findByEmail("zxzz11@naver.com");
        log.info("user : " + findUser);
        Assertions.assertThat(findUser.getEmail()).isEqualTo("zxzz11@naver.com");
    }

    @Test
    void removeUser() {
    }

    @Test
    void login() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void emailCheck() {
    }

    @Test
    void nickNameCheck() {
    }
}