package com.example.shopping.entity.cart;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartMainDTO;
import com.example.shopping.domain.cart.CartUpdateDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
import com.example.shopping.entity.item.ItemEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "cartitem")
@Getter
@ToString(exclude = "cart")
@NoArgsConstructor
public class CartItemEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long cartitemId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private CartEntity cart;

    @Column(name="cart_count")
    private int count;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Builder
    public CartItemEntity(Long cartitemId, CartEntity cart, ItemEntity item, int count) {
        this.cartitemId = cartitemId;
        this.cart =cart;
        this.item = item;
        this.count = count;
    }

    public CartMainDTO toMainDTO(){
        return CartMainDTO.builder()
                .itemId(this.item.getItemId())
                .count(this.count)
                .build();
    }

    public CartItemDTO toItemDTO(){
        return CartItemDTO.builder()
                .cartItemId(this.cartitemId)
                .cartId(this.cart.getCartId())
                .cart(this.cart.toDTO())
                .count(this.count)
                .item(ItemDTO.toItemDTO(item))
                .price(this.item.getPrice() * this.count)
                .mbrId(this.cart.getMember().getMemberId())
                .build();
    }

    public CartUpdateDTO toUpdateDTO(){
        return CartUpdateDTO.builder()
                .itemId(this.item.getItemId())
                .cartId(this.cart.getCartId())
                .count(this.count)
                .build();
    }

    public static CartItemEntity setCartItem(CartEntity cart, ItemEntity item, int count){
        return CartItemEntity.builder()
                .cart(cart)
                .item(item)
                .count(count)
                .build();
    }
}
