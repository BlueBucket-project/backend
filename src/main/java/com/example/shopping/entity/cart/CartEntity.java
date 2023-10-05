package com.example.shopping.entity.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "cart")
@Getter
@ToString
@NoArgsConstructor
public class CartEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name="cart_id")
    private Long cartId;

    @Column(name="member_id")
    private Long memberId;

    @OneToMany(mappedBy="cart")
    private List<CartItemEntity> cartItem;

    @Builder
    public CartEntity(Long cartId, Long memberId, List<CartItemEntity> cartItem) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.cartItem = cartItem;
    }

    public CartDTO toDTO(){
        return CartDTO.builder()
                .cartId(this.cartId)
                .memberId(this.memberId)
                .cartItem(this.cartItem.stream()
                        .map(CartItemEntity::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

}
