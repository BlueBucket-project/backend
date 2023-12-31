package com.example.shopping.entity.member;

import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.Base.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "member")
@Getter
@ToString
@NoArgsConstructor
public class MemberEntity extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void addPoint(int point){
        this.memberPoint += point;
    }
}
