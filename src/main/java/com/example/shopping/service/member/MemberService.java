package com.example.shopping.service.member;

import com.example.shopping.domain.member.RequestMemberDTO;
import com.example.shopping.domain.member.ResponseMemberDTO;
import com.example.shopping.domain.member.ModifyMemberDTO;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    // 회원가입
    ResponseEntity<?> signUp(RequestMemberDTO requestMemberDTO);
    // 회원조회
    ResponseMemberDTO search(Long memberId);
    // 회원삭제
    String removeUser(Long memberId, String email);
    // 로그인
    ResponseEntity<?> login(String memberEmail, String memberPw);
    // 회원수정
    ResponseEntity<?> updateUser(Long memberId, ModifyMemberDTO modifyMemberDTO, String memberEmail);
    // 이메일 체크
    String emailCheck(String email);
    // 닉네임 체크
    String nickNameCheck(String nickName);
}
