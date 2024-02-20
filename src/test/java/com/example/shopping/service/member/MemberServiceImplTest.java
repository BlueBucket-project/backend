package com.example.shopping.service.member;

import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.domain.jwt.TokenDTO;
import com.example.shopping.domain.member.RequestMemberDTO;
import com.example.shopping.domain.member.ResponseMemberDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.domain.member.UpdateMemberDTO;
import com.example.shopping.entity.cart.CartEntity;
import com.example.shopping.entity.jwt.TokenEntity;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.cart.CartJpaRepository;
import com.example.shopping.repository.comment.CommentRepository;
import com.example.shopping.repository.jwt.TokenRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@Log4j2
class MemberServiceImplTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private CartJpaRepository cartJpaRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private MemberServiceImpl memberService;


    private MemberEntity createMember() {

        return MemberEntity.builder()
                .memberId(1L)
                .memberPw("dudtjq8990!")
                .memberName("테스터")
                .memberRole(Role.USER)
                .nickName("테스터")
                .email("test@test.com")
                .memberPoint(0)
                .provider(null)
                .providerId(null)
                .address(AddressEntity.builder()
                        .memberAddr("서울시 강남구")
                        .memberZipCode("103-332")
                        .memberAddrDetail("102")
                        .build())
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUp() {
        // given
        MemberEntity member = createMember();
        log.info(member);
        // 가상의 존재하는 이메일
        MemberEntity haveEmail = MemberEntity.builder()
                .memberPw("dudtjq8990!")
                .memberName("테스터2")
                .memberRole(Role.USER)
                .nickName("테스터2")
                .email("test2@test.com")
                .memberPoint(0)
                .provider(null)
                .providerId(null)
                .address(AddressEntity.builder()
                        .memberAddr("서울시 강남구")
                        .memberZipCode("103-332")
                        .memberAddrDetail("102")
                        .build())
                .build();

        CartEntity cart = CartEntity.builder()
                .cartId(1L)
                .carItems(new ArrayList<>())
                .member(member)
                .build();

        RequestMemberDTO request = RequestMemberDTO.builder()
                .email(member.getEmail())
                .memberName(member.getMemberName())
                .nickName(member.getNickName())
                .memberPw(member.getMemberPw())
                .memberRole(member.getMemberRole())
                .build();

        String encode = "encodePasword!";
        log.info("인코드 :  " + encode);

        given(memberRepository.save(any())).willReturn(member);
        given(cartJpaRepository.save(any())).willReturn(cart);
        // 비밀번호 인코딩 시 모의(mock) 객체가 특정 문자열을 반환하도록 설정
        given(passwordEncoder.encode(anyString())).willReturn(encode);

        // when
        memberService.signUp(request);

        // then
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("로그인 기능 테스트")
    void login() {
        // given
        MemberEntity member = createMember();

        TokenDTO tokenDTO = TokenDTO.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .memberId(member.getMemberId())
                .grantType("Bearer ")
                .memberEmail(member.getEmail())
                .memberId(member.getMemberId())
                .build();

        TokenEntity tokenEntity = TokenEntity.tokenEntity(tokenDTO);

        given(memberRepository.findByEmail(anyString())).willReturn(member);
        given(tokenRepository.findByMemberEmail(anyString())).willReturn(null);
        given(jwtProvider.createToken(any(Authentication.class), any(), anyLong())).willReturn(tokenDTO);
        given(tokenRepository.save(any())).willReturn(tokenEntity);
        // 비밀번호 매칭 시 모의(mock) 객체가 매칭 여부를 true로 설정
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);


        // when
        memberService.login(member.getEmail(), member.getMemberPw());

        // then
        verify(tokenRepository).save(any());
    }

    @Test
    @DisplayName("조회하는 기능 테스트")
    void search() {
        // given
        MemberEntity member = createMember();
        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));

        // when
        memberService.search(1L);

        // then
        verify(memberRepository).findById(1L);
    }

    @Test
    @DisplayName("삭제하는 기능 테스트")
    void removeUser() {
        // given
        MemberEntity member = createMember();
        given(memberRepository.findByEmail(anyString())).willReturn(member);
        doNothing().when(boardRepository).deleteAllByMemberMemberId(anyLong());
        doNothing().when(cartJpaRepository).deleteAllByMemberMemberId(anyLong());
        doNothing().when(commentRepository).deleteAllByMemberMemberId(anyLong());
        doNothing().when(memberRepository).deleteByMemberId(anyLong());

        // when
        memberService.removeUser(1L, member.getEmail());

        // then
        verify(boardRepository).deleteAllByMemberMemberId(1L);
        verify(cartJpaRepository).deleteAllByMemberMemberId(1L);
        verify(commentRepository).deleteAllByMemberMemberId(1L);
        verify(memberRepository).deleteByMemberId(1L);
    }


    @Test
    @DisplayName("업데이트 기능 테스트")
    void updateUser() {
        // given
        MemberEntity member = createMember();

        UpdateMemberDTO request = UpdateMemberDTO.builder()
                .nickName("수정된닉네임")
                .memberPw(null)
                .memberAddress(null)
                .build();

        member.updateMember(request, null);

        given(memberRepository.findByEmail(anyString())).willReturn(member);
        given(memberRepository.findByNickName(eq(member.getNickName()))).willReturn(member);
        given(memberRepository.findByNickName(not(eq(member.getNickName())).toString())).willReturn(null);
        given(memberRepository.save(any())).willReturn(member);

        // when
        ResponseEntity<?> responseEntity = memberService.updateUser(1L, request, "test@test.com");
        ResponseMemberDTO body = (ResponseMemberDTO) responseEntity.getBody();

        // then
        verify(memberRepository).save(any());
        Assertions.assertThat(Objects.requireNonNull(body).getEmail()).isEqualTo(member.getEmail());
        Assertions.assertThat(body.getMemberName()).isEqualTo(member.getMemberName());
        Assertions.assertThat(body.getNickName()).isNotEqualTo(member.getNickName());
}
}