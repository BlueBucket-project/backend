package com.example.shopping.entity.member;

import lombok.*;

import javax.persistence.Embeddable;

/*
 *   writer : 유요한
 *   work :
 *          주소에 대한 값을 값타입 엔티티를 사용해서 받음
 *          이렇게 한 이유는 나중에 필요할 때 재활용하기 위해서
 *          그리고 유지보수하기 좋게 하기 위해서입니다.
 *   date : 2023/10/18
 * */
// 임베디드 타입을 사용하려면 넣어야한다.
@Embeddable
@ToString
@Getter
@NoArgsConstructor
public class AddressEntity {
    private String memberAddr;
    private String memberAddrDetail;
    private String memberZipCode;

    @Builder
    public AddressEntity(String memberAddr, String memberAddrDetail, String memberZipCode) {
        this.memberAddr = memberAddr;
        this.memberAddrDetail = memberAddrDetail;
        this.memberZipCode = memberZipCode;
    }
}