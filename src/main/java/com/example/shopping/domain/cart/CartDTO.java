package com.example.shopping.domain.cart;

import com.example.shopping.domain.member.InfoMemberDTO;
import com.example.shopping.entity.cart.CartEntity;
import com.example.shopping.entity.cart.CartItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/*
 *   writer : 오현진
 *   work :
 *          장바군니 리턴 값을 담고 있습니다.
 *   date : 2023/12/08
 * */
@Getter
@NoArgsConstructor
@ToString
public class CartDTO {

    @Schema(description = "장바구니번호")
    private Long cartId;

    private InfoMemberDTO member;

    private List<CartItemDTO> cartItems = new ArrayList<>();

    @Builder
    public CartDTO(Long cartId, InfoMemberDTO member, List<CartItemDTO> cartItems) {
        this.cartId = cartId;
        this.member = member;
        this.cartItems = cartItems;
    }


    // OneToMany cartItems 셋팅 (기존분 변경 시 사용)
    public CartEntity toEntity(){
        return CartEntity.builder()
                .cartId(this.cartId)
                .member(member==null?null :
                        MemberEntity.builder().memberId(member.getId()).build())
                .carItems(this.cartItems==null?null:this.cartItems.stream()
                        .map(CartItemDTO::toEntity).collect(Collectors.toList()))
                .build();
    }

    // OneToMany cartItems 미셋팅 (Cart부터 새로 등록할 때 사용)
    public static CartEntity toNewEntity(CartDTO cart){
        return CartEntity.builder()
                .cartId(cart.getCartId())
                .member(cart.getMember()==null? null :
                        MemberEntity.builder().memberId(cart.getMember().getId()).build())
                .build();
    }
    public static CartDTO toCartDTO(CartEntity cartEntity){
        List<CartItemEntity> cartItemEntityList = cartEntity.getCartItems();
        List<CartItemDTO> cartItemDTOList = new ArrayList<>();

        if(cartItemEntityList != null){
            for(CartItemEntity item : cartItemEntityList){
                cartItemDTOList.add(CartItemDTO.toDTO(item));
            }
        }

        return CartDTO.builder()
                .cartId(cartEntity.getCartId())
                .cartItems(cartItemDTOList)
                .member(InfoMemberDTO.from(cartEntity.getMember()))
                .build();
    }

    public static CartDTO createCart(MemberEntity member){
        return CartDTO.builder()
                .member(InfoMemberDTO.from(member))
                .cartItems(new ArrayList<>())
                .build();
    }

    public void addCartItems(CartItemDTO cartItem){
        if(this.cartItems == null){
            this.cartItems = new ArrayList<>();
        }
        this.cartItems.add(cartItem);
    }

    // 이거는 리스트를 조절하는 메소드
    public void updateCartItems(CartItemDTO cartItem){
        int idx=0;

        for(CartItemDTO item : this.cartItems){
            // 받아온 상품id와 기존의 id를 비교해서 맞다면 true
            if(Objects.equals(item.getCartItemId(), cartItem.getCartItemId())){
                // 리스트에서 삭제
                this.cartItems.remove(idx);
                // 리스트에 추가
                this.cartItems.add(cartItem);
                break;
            }
            // id를 비교해서 맞지 않다면 index를 증가시켜준다.
            idx++;
        }
    }

    public void addList(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
