package com.example.shopping.entity.cart;

import com.example.shopping.domain.cart.*;
import com.example.shopping.entity.Base.BaseTimeEntity;
import com.example.shopping.entity.item.ItemEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 *   writer : 오현진
 *   work :
 *          장바구니 상품 테이블을 만들어줍니다.
 *   date : 2024/12/08
 * */
@Entity(name = "cartitem")
@Getter
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

    @Enumerated(EnumType.STRING)
    @Column(name="item_status")
    private CartStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Builder
    public CartItemEntity(Long cartitemId,
                          CartEntity cart,
                          CartStatus status,
                          ItemEntity item,
                          int count) {
        this.cartitemId = cartitemId;
        this.cart =cart;
        this.item = item;
        this.status = status;
        this.count = count;
    }
}
