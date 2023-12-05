package com.example.shopping.domain.member;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ToString
@Getter
@NoArgsConstructor
public class ModifyMemberDTO {
    @Schema(description = "닉네임")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String nickName;

    @Schema(description = "회원 비밀번호")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*\\\\W)(?=\\\\S+$).{8,20}$", message = "비밀번호는 영문 소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8 ~20자의 비밀번호여야 합니다." )
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
