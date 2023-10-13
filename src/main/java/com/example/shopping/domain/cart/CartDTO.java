package com.example.shopping.domain.cart;

import com.example.shopping.domain.member.MemberDTO;
import com.example.shopping.entity.cart.CartEntity;
import com.example.shopping.entity.member.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CartDTO {

    @Schema(description = "장바구니번호")
    private Long cartId;

    private MemberDTO member;

    private List<CartMainDTO> cartItem = new ArrayList<>();

    @Builder
    public CartDTO(Long cartId, MemberDTO member, List<CartMainDTO> cartItem) {
        this.cartId = cartId;
        this.member = member;
        this.cartItem = cartItem;
    }

    public CartEntity toEntity(){
        return CartEntity.builder()
                .cartId(this.cartId)
                .member(member.toMemberInfoEntity())
                .build();
    }

    public CartDTO createCart(MemberEntity member){

        return CartDTO.builder()
                .cartId(this.cartId)
                .member(MemberDTO.toMemberDTO(member))
                .cartItem(this.cartItem)
                .build();
    }
}
