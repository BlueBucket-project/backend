package com.example.shopping.domain.member;

import com.example.shopping.entity.member.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/*
 *   writer : 유요한
 *   work :
 *          유저에 대한 필요한 정보를 받는 RequestDTO
 *   date : 2023/11/16
 * */
@ToString
@Getter
@NoArgsConstructor
public class InfoMemberDTO {

    @Schema(description = "아이디")
    private Long id;
    @Schema(description = "닉네임")
    private String nickName;
    @Schema(description = "이메일")
    private String email;

    @Builder
    public InfoMemberDTO(Long id, String nickName, String email) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
    }

    public static InfoMemberDTO toInfoMember(ResponseMemberDTO member){
        return InfoMemberDTO.builder()
                .id(member.getMemberId())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .build();
    }

    public static InfoMemberDTO from(MemberEntity member){
        return InfoMemberDTO.builder()
                .id(member.getMemberId())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .build();
    }
}
