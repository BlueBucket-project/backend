package com.example.shopping.domain.cart;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.member.InfoMemberDTO;
import com.example.shopping.domain.member.ResponseMemberDTO;
import com.example.shopping.entity.cart.CartEntity;
import com.example.shopping.entity.cart.CartItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.entity.member.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
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

    public CartEntity toEntity(){
        return CartEntity.builder()
                .cartId(this.cartId)
                .member(member==null?null:MemberEntity.builder()
                        .memberId(member.getId())
                        .build())
                .carItems(this.cartItems==null?null:this.cartItems.stream()
                        .map(CartItemDTO::toEntity).collect(Collectors.toList()))
                .build();
    }

    public static CartEntity toCartEntity(CartDTO cart){
        return CartEntity.builder()
                .cartId(cart.getCartId())
                .member(cart.getMember()==null? null : MemberEntity.builder().memberId(cart.getMember().getId()).build())
                .carItems(cart.getCartItems()==null ? null : cart.getCartItems().stream()
                        .map(CartItemDTO::toEntity).collect(Collectors.toList()))
                .build();
    }
    public static CartDTO toCartDTO(CartEntity cartEntity){
        List<CartItemEntity> cartItemEntityList = cartEntity.getCartItems();
        List<CartItemDTO> cartItemDTOList = new ArrayList<>();

        for(CartItemEntity item : cartItemEntityList){
            cartItemDTOList.add(CartItemDTO.toDTO(item));
        }

        return CartDTO.builder()
                .cartId(cartEntity.getCartId())
                .cartItems(cartItemDTOList)
                .member(InfoMemberDTO.from(cartEntity.getMember()))
                .build();
    }

    public CartDTO createCart(MemberEntity member){
        return CartDTO.builder()
                .cartId(this.cartId)
                .member(InfoMemberDTO.from(member))
                .cartItems(new ArrayList<>())
                .build();
    }

    public void addCartItems(CartItemDTO cartItem){
        this.cartItems.add(cartItem);
    }

    public void updateCartItems(CartItemDTO cartItem){
        int idx=0;

        for(CartItemDTO item:this.cartItems){
            if(item.getCartItemId() == cartItem.getCartItemId()){
                this.cartItems.remove(idx);
                this.cartItems.add(cartItem);
                break;
            }
            idx++;
        }
    }
}
