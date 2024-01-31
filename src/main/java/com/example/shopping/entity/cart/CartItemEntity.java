package com.example.shopping.entity.cart;

import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.cart.*;
import com.example.shopping.entity.Base.BaseTimeEntity;
import com.example.shopping.entity.item.ItemEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 *   writer : 오현진, 유요한
 *   work :
 *          장바구니 상품 테이블을 만들어줍니다.
 *   date : 2024/01/29
 * */
@Entity(name = "cartitem")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    public CartItemEntity(Long cartItemId,
                          CartEntity cart,
                          CartStatus status,
                          ItemEntity item,
                          int count) {
        this.cartitemId = cartItemId;
        this.cart =cart;
        this.item = item;
        this.status = status;
        this.count = count;
    }

    // 예약 시 상품의 상태, 예약자 그리고 예약 수량을 수정하기 위해서
    // 여기에 처리한 이유는 더티 체킹을 이용하기 위해서 입니다.
    public void orderItem(String email) {
        this.getItem().changeStatus(ItemSellStatus.RESERVED);
        this.getItem().reserveItem(email, this.count);
    }

    public void cancelOrderItem() {
        // 상품 엔티티의 상태 변경 메소드 호출
        this.getItem().changeStatus(ItemSellStatus.SELL);
        this.getItem().reserveItem(null, 0);
    }
}
