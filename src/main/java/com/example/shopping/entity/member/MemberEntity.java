package com.example.shopping.entity.member;

import com.example.shopping.domain.member.UpdateMemberDTO;
import com.example.shopping.domain.member.RequestMemberDTO;
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

    // 저장
    public static MemberEntity saveMember(RequestMemberDTO member, String password) {
        return MemberEntity.builder()
                .email(member.getEmail())
                .memberPw(password)
                .memberName(member.getMemberName())
                .nickName(member.getNickName())
                .memberRole(member.getMemberRole())
                .address(AddressEntity.builder()
                        .memberAddr(member.getMemberAddress().getMemberAddr() == null
                                ? null : member.getMemberAddress().getMemberAddr())
                        .memberAddrDetail(member.getMemberAddress().getMemberAddrDetail() == null
                                ? null : member.getMemberAddress().getMemberAddrDetail())
                        .memberZipCode(member.getMemberAddress().getMemberZipCode() == null
                                ? null : member.getMemberAddress().getMemberZipCode())
                        .build()).build();
    }

    public void updateMember(UpdateMemberDTO updateMember, String encodePw) {
        this.memberPw = updateMember.getMemberPw() == null ? this.memberPw : encodePw;
        this.nickName = updateMember.getNickName() == null ? this.nickName : updateMember.getNickName();

        // 기존 주소 엔티티를 직접 수정
        if (updateMember.getMemberAddress() != null) {
            this.address = AddressEntity.builder()
                    .memberAddr(updateMember.getMemberAddress().getMemberAddr())
                    .memberAddrDetail(updateMember.getMemberAddress().getMemberAddrDetail())
                    .memberZipCode(updateMember.getMemberAddress().getMemberZipCode())
                    .build();
        } else {
            this.address = null;
        }
    }


    public void addPoint(int point) {
        this.memberPoint += point;
    }
}
