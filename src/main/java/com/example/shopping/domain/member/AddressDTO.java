package com.example.shopping.domain.member;

import com.example.shopping.entity.member.AddressEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/*
 *   writer : 유요한
 *   work :
 *          주소에 대한 ResponseDTO
 *   date : 2023/11/01
 * */
@ToString
@Getter
@NoArgsConstructor
public class AddressDTO {
    @Schema(description = "우편번호")
    private String memberAddr;
    @Schema(description = "주소")
    private String memberAddrDetail;
    @Schema(description = "상세 주소")
    private String memberZipCode;

    @Builder
    public AddressDTO(String memberAddr, String memberAddrDetail, String memberZipCode) {
        this.memberAddr = memberAddr;
        this.memberAddrDetail = memberAddrDetail;
        this.memberZipCode = memberZipCode;
    }

    // 주소 엔티티 변환 추가
    public AddressEntity toEntity(){
        return AddressEntity.builder()
                .memberAddr(this.memberAddr)
                .memberAddrDetail(this.memberAddrDetail)
                .memberZipCode(this.memberZipCode)
                .build();
    }
}
