package com.example.shopping.domain.cart;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.entity.cart.CartItemEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemDTO {

    private Long cartItemId;
    private int price;
    private int count;
    private Long cartId;
    private CartDTO cart;
    private ItemDTO item;
    private Long mbrId;

    @Builder
    public CartItemDTO(Long cartItemId, int price, int count, Long cartId, Long mbrId, CartDTO cart, ItemDTO item){
        this.cartItemId = cartItemId;
        this.price = price;
        this.count = count;
        this.cartId = cartId;
        this.cart = cart;
        this.item = item;
        this.mbrId = mbrId;
    }

    public CartItemEntity toEntity(){
        return CartItemEntity.builder()
                .cartitemId(this.cartItemId)
                .count(this.count)
                .cart(this.cart.toEntity())
                .item(this.item.toEntity())
                .build();
    }

    public void modifyCount(int cnt){
        this.count = cnt;
    }

}
