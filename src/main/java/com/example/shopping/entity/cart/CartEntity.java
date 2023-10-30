package com.example.shopping.entity.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.domain.member.ResponseMemberDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "cart")
@Getter
@ToString
@NoArgsConstructor
public class CartEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Long cartId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Builder
    public CartEntity(Long cartId, MemberEntity member) {
        this.cartId = cartId;
        this.member = member;
    }

    public CartDTO toDTO(){
        return CartDTO.builder()
                .cartId(this.cartId)
                .member(ResponseMemberDTO.toMemberDTO(this.member))
                .build();
    }

}
