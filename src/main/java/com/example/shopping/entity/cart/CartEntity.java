package com.example.shopping.entity.cart;

import com.example.shopping.entity.Base.BaseTimeEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "cart")
@Getter
@NoArgsConstructor
public class CartEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Long cartId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItemEntity> cartItems = new ArrayList<>();

    @Builder
    public CartEntity(Long cartId, MemberEntity member, List<CartItemEntity> carItems) {
        this.cartId = cartId;
        this.member = member;
        this.cartItems = carItems;
    }

}
