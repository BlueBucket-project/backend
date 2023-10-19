package com.example.shopping.domain.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@ToString
@NoArgsConstructor
public class RequestMemberDTO {
    @Schema(description = "이메일", example = "abc@gmail.com", required = true)
    @NotNull(message = "이메일은 필수 입력입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Schema(description = "회원 이름")
    @NotNull(message = "이름은 필수 입력입니다.")
    private String memberName;

    @Schema(description = "닉네임")
    @NotNull(message = "닉네임은 필수 입력입니다.")
    private String nickName;

    @Schema(description = "회원 비밀번호")
    private String memberPw;

    @Schema(description = "회원 권한")
    @NotNull(message = "권한정보는 필수 입력입니다.")
    private Role memberRole;

    @Schema(description = "회원 주소")
    private AddressDTO memberAddress;

    @Builder
    public RequestMemberDTO(String email,
                            String memberName,
                            String nickName,
                            String memberPw,
                            Role memberRole,
                            AddressDTO memberAddress) {
        this.email = email;
        this.memberName = memberName;
        this.nickName = nickName;
        this.memberPw = memberPw;
        this.memberRole = memberRole;
        this.memberAddress = memberAddress;
    }
}
