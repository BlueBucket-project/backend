package com.example.shopping.domain.cart;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.entity.cart.CartEntity;
import com.example.shopping.entity.cart.CartItemEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemDTO {

    private Long cartItemId;
    private int price;
    private int count;
    //private Long cartId;
    private CartDTO cart;
    private ItemDTO item;
    private Long mbrId;

    @Builder
    public CartItemDTO(Long cartItemId, int price, int count, CartDTO cart, Long mbrId, ItemDTO item){
        this.cartItemId = cartItemId;
        this.price = price;
        this.count = count;
        this.cart = cart;
        this.item = item;
        this.mbrId = mbrId;
    }

    public CartItemEntity toEntity(){
        return CartItemEntity.builder()
                .cartitemId(this.cartItemId)
                .count(this.count)
                .cart(this.cart == null? null :
                        CartEntity.builder().cartId(this.cart.getCartId()).build())
                .item(this.item.toEntity())
                .build();
    }

    public static CartItemDTO toDTO(CartItemEntity cartItemEntity){
        return CartItemDTO.builder()
                .cartItemId(cartItemEntity.getCartitemId())
                .count(cartItemEntity.getCount())
                .item(ItemDTO.toItemDTO(cartItemEntity.getItem()))
                .price(cartItemEntity.getCount() * cartItemEntity.getItem().getPrice())
                .cart(cartItemEntity.getCart()==null? null :
                        CartDTO.builder().cartId(cartItemEntity.getCart().getCartId()).build())
                .mbrId(cartItemEntity.getCart().getMember()==null?null:
                        cartItemEntity.getCart().getMember().getMemberId())
                .build();
    }

    public void modifyCount(int cnt){
        this.count = cnt;
    }

    public static CartItemDTO setCartItem(CartDTO cart, ItemEntity item, int count){
        return CartItemDTO.builder()
                .cart(cart)
                .item(ItemDTO.toItemDTO(item))
                .price(item.getPrice() * count)
                .count(count)
                .mbrId(cart.getMember().getId())
                .build();
    }

}
