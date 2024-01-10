package com.example.shopping.entity.member;

import com.example.shopping.domain.member.ModifyMemberDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.Base.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

/*
 *   writer : 유요한
 *   work :
 *          유저 엔티티
 *   date : 2024/01/10
 * */
@Entity(name = "member")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_email", nullable = false)
    private String email;

    @Column(name = "member_pw")
    private String memberPw;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    // USER, ADMIN
    private Role memberRole;

    private String provider;
    private String providerId;

    @Embedded
    private AddressEntity address;
    private int memberPoint;

    @Builder
    public MemberEntity(Long memberId,
                        String memberName,
                        String email,
                        String memberPw,
                        String nickName,
                        Role memberRole,
                        String provider,
                        String providerId,
                        AddressEntity address,
                        int memberPoint) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.email = email;
        this.memberPw = memberPw;
        this.nickName = nickName;
        this.memberRole = memberRole;
        this.provider = provider;
        this.providerId = providerId;
        this.address = address;
        this.memberPoint = memberPoint;
    }

    public void updateMember(ModifyMemberDTO updateMember, String encodePw) {
        MemberEntity.builder()
                .memberId(this.memberId)
                .email(this.email)
                .memberPw(updateMember.getMemberPw() == null
                        ? this.memberPw
                        : encodePw)
                .nickName(updateMember.getNickName() == null
                ? this.getNickName() : updateMember.getNickName())
                .memberRole(this.memberRole)
                .memberPoint(this.memberPoint)
                .memberName(this.memberName)
                .address(AddressEntity.builder()
                        .memberAddr(updateMember.getMemberAddress().getMemberAddr() == null
                        ? this.address.getMemberAddr() : updateMember.getMemberAddress().getMemberAddr())
                        .memberAddrDetail(updateMember.getMemberAddress().getMemberAddrDetail() == null
                        ? this.address.getMemberAddrDetail() : updateMember.getMemberAddress().getMemberAddrDetail())
                        .memberZipCode(updateMember.getMemberAddress().getMemberZipCode() == null
                        ? this.address.getMemberZipCode() : updateMember.getMemberAddress().getMemberZipCode())
                        .build()).build();
    }


    public void addPoint(int point) {
        this.memberPoint += point;
    }
}
