package com.example.shopping.domain.member;

import com.example.shopping.entity.member.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
/*
 *   writer : 유요한, 오현진
 *   work :
 *          유저에 대한 정보를 담은 ResponseDTO
 *   date : 2023/11/28
 * */
@ToString
@Getter
@NoArgsConstructor
public class ResponseMemberDTO {
    @Schema(description = "유저 번호", example = "1", required = true)
    private Long memberId;

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

    @Schema(description = "소셜 로그인")
    private String provider;        // ex) google

    @Schema(description = "소셜 로그인 식별 아이디")
    private String providerId;

    @Schema(description = "포인트")
    private int memberPoint;

    @Builder
    public ResponseMemberDTO(Long memberId,
                             String email,
                             String memberName,
                             String nickName,
                             String memberPw,
                             Role memberRole,
                             AddressDTO memberAddress,
                             String provider,
                             String providerId,
                             int memberPoint) {
        this.memberId = memberId;
        this.email = email;
        this.memberName = memberName;
        this.nickName = nickName;
        this.memberPw = memberPw;
        this.memberRole = memberRole;
        this.memberAddress = memberAddress;
        this.provider = provider;
        this.providerId = providerId;
        this.memberPoint = memberPoint;
    }

    // 엔티티를 DTO로 반환
    public static ResponseMemberDTO toMemberDTO(MemberEntity member) {
        return ResponseMemberDTO.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .memberPw(member.getMemberPw())
                .nickName(member.getNickName())
                .memberName(member.getMemberName())
                .memberRole(member.getMemberRole())
                .memberPoint(member.getMemberPoint())
                .memberAddress(AddressDTO.builder()
                        .memberAddr(member.getAddress() == null
                                ? null : member.getAddress().getMemberAddr())
                        .memberAddrDetail(member.getAddress() == null
                                ? null : member.getAddress().getMemberAddrDetail())
                        .memberZipCode(member.getAddress() == null
                                ? null : member.getAddress().getMemberZipCode())
                        .build()).build();
    }
    // 소셜 로그인시 필요한 정보만 전달하기 위해서
    public static ResponseMemberDTO socialMember(MemberEntity member) {
        return ResponseMemberDTO.builder()
                .provider(member.getProvider())
                .providerId(member.getProviderId())
                .build();
    }

    public MemberEntity toMemberInfoEntity() {
        return MemberEntity.builder()
                .memberId(this.memberId)
                .memberName(this.memberName)
                .memberPw(this.memberPw)
                .email(this.email)
                .nickName(this.nickName)
                .memberPoint(this.memberPoint)
                .memberRole(this.memberRole)
                .provider(this.provider)
                .providerId(this.providerId)
                .address(this.memberAddress.toEntity())
                .build();
    }
}
