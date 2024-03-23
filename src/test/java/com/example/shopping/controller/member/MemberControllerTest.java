package com.example.shopping.controller.member;

import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.jwt.TokenDTO;
import com.example.shopping.domain.member.LoginDTO;
import com.example.shopping.domain.member.RequestMemberDTO;
import com.example.shopping.domain.member.Role;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.shopping.domain.member.UpdateMemberDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.entity.Container.ContainerEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.service.board.BoardService;
import com.example.shopping.service.jwt.TokenService;
import com.example.shopping.service.member.MemberService;
import com.example.shopping.service.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Log4j2
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private MemberService memberService;
    @MockBean
    private TokenService tokenService;

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


    private TokenDTO createToken() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(createMember().getEmail(), createMember().getMemberPw());
        log.info("authentication : " + authentication);
        List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(createMember());

        // 토큰 생성
        TokenDTO token = jwtProvider.createToken(authentication, authoritiesForUser, createMember().getMemberId());
        log.info("토큰 : " + token);
        return token;
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
    @DisplayName("회원가입 테스트")
    void join() throws Exception {
        RequestMemberDTO member = RequestMemberDTO.builder()
                .email("test@test.com")
                .memberPw("dudtjq8990!")
                .memberName("테스트중")
                .nickName("테스트중")
                .memberRole(Role.USER)
                .build();

        String request = objectMapper.writeValueAsString(member);

        mockMvc.perform(
                        post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 조회 테스트")
    void search() throws Exception {
        Long id = 1L;

        mockMvc.perform(
                        get("/api/v1/users/{memberId}", id))
                .andExpect(status().isOk());

        verify(memberService).search(id);
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    @WithMockUser(username = "test@test.com", password = "dudtjq8990!", roles = "USER")
    void remove() throws Exception {
        Long id = 1L;
        String email = "test@test.com";

        mockMvc.perform(delete("/api/v1/users/{memberId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + createToken().getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).removeUser(id, email);
    }

    @Test
    void login() throws Exception {
        LoginDTO login = LoginDTO.builder()
                .memberEmail("test@test.com")
                .memberPw("dudtjq8990!")
                .build();

        mockMvc.perform(
                        post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).login(login.getMemberEmail(), login.getMemberPw());
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "dudtjq8990!", roles = "USER")
    void update() throws Exception {
        Long id = 1L;
        UpdateMemberDTO update = UpdateMemberDTO.builder()
                .nickName("수정닉네임")
                .memberPw("dudtjq8990!")
                .memberAddress(null)
                .build();

        // when
        mockMvc
                .perform(put("/api/v1/users/{memberId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
                        .header("Authorization", "Bearer " + createToken().getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(memberService).updateUser(refEq(id), refEq(update), refEq(createMember().getEmail()));
    }

    @Test
    void refreshToken() throws Exception {
        String email = createMember().getEmail();

        mockMvc
                .perform(get("/api/v1/users/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(email))
                        .header("Authorization", "Bearer " + createToken().getRefreshToken()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(tokenService).createAccessToken(createToken().getRefreshToken());
    }

    @Test
    void emailCheck() throws Exception {
        String email = createMember().getEmail();

        mockMvc
                .perform(get("/api/v1/users/email/{memberEmail}", email))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void nickNameCheck() throws Exception {
        String nickName = createMember().getNickName();

        mockMvc
                .perform(get("/api/v1/users/nickName/{nickName}", nickName))
                .andExpect(status().isOk())
                .andDo(print());
    }


}