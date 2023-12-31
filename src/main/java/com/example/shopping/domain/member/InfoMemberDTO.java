package com.example.shopping.domain.member;

import com.example.shopping.entity.member.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 필요한 정보용
// 즉, 기능을 구현할 때 필요한 정보만 받아와서 처리하기 위해서
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
