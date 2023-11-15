package com.example.shopping.service.member;

import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.domain.jwt.TokenDTO;
import com.example.shopping.domain.member.RequestMemberDTO;
import com.example.shopping.domain.member.ResponseMemberDTO;
import com.example.shopping.domain.member.ModifyMemberDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.jwt.TokenEntity;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.jwt.TokenRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    // 회원가입
    @Override
    public ResponseEntity<?> signUp(RequestMemberDTO memberDTO) {
        try {
            log.info("email : " + memberDTO.getEmail());
            log.info("nickName : " + memberDTO.getNickName());

            if (!emailCheck(memberDTO.getEmail()) && !nickNameCheck(memberDTO.getNickName())) {
                return ResponseEntity.badRequest().body("이미 존재하는 회원이 있습니다.");
            } else {
                // 아이디가 없다면 DB에 등록해줍니다.
                MemberEntity member = MemberEntity.builder()
                        .email(memberDTO.getEmail())
                        .memberPw(passwordEncoder.encode(memberDTO.getMemberPw()))
                        .memberName(memberDTO.getMemberName())
                        .nickName(memberDTO.getNickName())
                        .memberRole(memberDTO.getMemberRole())
                        .address(AddressEntity.builder()
                                .memberAddr(memberDTO.getMemberAddress().getMemberAddr())
                                .memberAddrDetail(memberDTO.getMemberAddress().getMemberAddrDetail())
                                .memberZipCode(memberDTO.getMemberAddress().getMemberZipCode())
                                .build()).build();

                log.info("member in service : " + member);
                MemberEntity saveMember = memberRepository.save(member);

                ResponseMemberDTO coverMember = ResponseMemberDTO.toMemberDTO(saveMember);
                return ResponseEntity.ok().body(coverMember);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 회원 조회
    @Override
    public ResponseMemberDTO search(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        return ResponseMemberDTO.toMemberDTO(member);
    }

    // 회원 삭제
    @Override
    public String removeUser(Long memberId, String email) {
        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(email);
        log.info("email check : " + email);
        log.info("email check2 : " + findUser.getEmail());

        // 회원이 비어있지 않고 넘어온 id가 DB에 등록된 id가 일치할 때
        if (findUser != null) {
            memberRepository.deleteByMemberId(memberId);
            return "회원 탈퇴 완료";
        } else {
            return "유저가 등록되어 있지 않습니다.";
        }
    }

    // 로그인
    @Override
    public ResponseEntity<?> login(String memberEmail, String memberPw) {
        try {
            // 회원 조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("user : " + findUser);

            if (findUser != null) {
                // DB에 넣어져 있는 비밀번호는 암호화가 되어 있어서 비교하는 기능을 사용해야 합니다.
                // 사용자가 입력한 패스워드를 암호화하여 사용자 정보와 비교
                if (passwordEncoder.matches(memberPw, findUser.getMemberPw())) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(memberEmail, memberPw);
                    log.info("authentication : " + authentication);
                    List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findUser);

                    // JWT 생성
                    TokenDTO token = jwtProvider.createToken(authentication, authoritiesForUser, findUser.getMemberId());
                    // 토큰 조회
                    TokenEntity findToken = tokenRepository.findByMemberEmail(token.getMemberEmail());

                    // 토큰이 없다면 새로 발급
                    if (findToken == null) {
                        log.info("발급한 토큰이 없습니다. 새로운 토큰을 발급합니다.");

                        // 토큰 생성과 조회한 memberId를 넘겨줌
                        TokenEntity tokenEntity = TokenEntity.tokenEntity(token);
                        // 토큰id는 자동생성
                        tokenRepository.save(tokenEntity);
                    } else {
                        log.info("이미 발급한 토큰이 있습니다. 토큰을 업데이트합니다.");
                        token = TokenDTO.builder()
                                .grantType(token.getGrantType())
                                .accessToken(token.getAccessToken())
                                .accessTokenTime(token.getAccessTokenTime())
                                .refreshToken(token.getRefreshToken())
                                .refreshTokenTime(token.getRefreshTokenTime())
                                .memberEmail(token.getMemberEmail())
                                .memberId(token.getMemberId())
                                .build();
                        // 이미 존재하는 토큰이니 토큰id가 있다.
                        // 그 id로 토큰을 업데이트 시켜준다.
                        TokenEntity tokenEntity = TokenEntity.updateToken(findToken.getId(), token);
                        tokenRepository.save(tokenEntity);
                    }
                    return ResponseEntity.ok().body(token);
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저가 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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

    // 회원정보 수정
    @Override
    public ResponseEntity<?> updateUser(Long memberId, ModifyMemberDTO modifyMemberDTO, String memberEmail) {
        try {
            // 회원조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("user : " + findUser);

            findUser = MemberEntity.builder()
                    .memberId(findUser.getMemberId())
                    .email(findUser.getEmail())
                    .memberPw(
                            modifyMemberDTO.getMemberPw() == null
                                    ? findUser.getMemberPw()
                                    : passwordEncoder.encode(modifyMemberDTO.getMemberPw()))
                    .nickName(modifyMemberDTO.getNickName() == null || !nickNameCheck(modifyMemberDTO.getNickName())
                            ? findUser.getNickName() : modifyMemberDTO.getNickName())
                    .memberRole(findUser.getMemberRole())
                    .memberPoint(findUser.getMemberPoint())
                    .memberName(modifyMemberDTO.getMemberName() == null
                    ? findUser.getMemberName() : modifyMemberDTO.getMemberName())
                    .address(AddressEntity.builder()
                            .memberAddr(modifyMemberDTO.getMemberAddress().getMemberAddr() == null
                                    ? findUser.getAddress().getMemberAddr()
                                    : modifyMemberDTO.getMemberAddress().getMemberAddr())
                            .memberAddrDetail(modifyMemberDTO.getMemberAddress().getMemberAddrDetail() == null
                                    ? findUser.getAddress().getMemberAddrDetail()
                                    : modifyMemberDTO.getMemberAddress().getMemberAddrDetail())
                            .memberZipCode(modifyMemberDTO.getMemberAddress().getMemberZipCode() == null
                                    ? findUser.getAddress().getMemberZipCode()
                                    : modifyMemberDTO.getMemberAddress().getMemberZipCode())
                            .build()).build();
            log.info("유저 수정 : " + findUser);

            MemberEntity updateUser = memberRepository.save(findUser);
            ResponseMemberDTO toResponseMemberDTO = ResponseMemberDTO.toMemberDTO(updateUser);
            return ResponseEntity.ok().body(toResponseMemberDTO);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보가 없습니다.");
        }
    }

    // 중복체크
    @Override
    public boolean emailCheck(String email) {
        MemberEntity findEmail = memberRepository.findByEmail(email);
        if (findEmail == null) {
            return true;
        } else {
            return false;
        }
    }

    // 닉네임 체크
    @Override
    public boolean nickNameCheck(String nickName) {
        MemberEntity findNickName = memberRepository.findByNickName(nickName);
        if (findNickName == null) {
            return true;
        } else {
            return false;
        }
    }


}
