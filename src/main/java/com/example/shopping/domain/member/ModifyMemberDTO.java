package com.example.shopping.domain.member;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class ModifyMemberDTO {
    @Schema(description = "닉네임")
    private String nickName;

    @Schema(description = "회원 비밀번호")
    private String memberPw;

    @Schema(description = "회원 주소")
    private AddressDTO memberAddress;

    @Builder
    public ModifyMemberDTO(String nickName, String memberPw, AddressDTO memberAddress) {
        this.nickName = nickName;
        this.memberPw = memberPw;
        this.memberAddress = memberAddress;
    }
}
