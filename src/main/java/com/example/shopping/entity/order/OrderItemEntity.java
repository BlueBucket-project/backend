package com.example.shopping.entity.order;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.entity.item.ItemEntity;
import lombok.*;

import javax.persistence.*;
/*
 *   writer : 오현진
 *   work :
 *          주소에 대한 ResponseDTO
 *   date : 2023/12/08
 * */
@Entity(name = "orderitem")
@Getter
@ToString(exclude = "order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long orderitemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name="item_buyer")
    private Long itemBuyer;
    @Column(name="item_seller")
    private Long itemSeller;

    @Column(name="item_amount")
    int count;

    @Builder
    public OrderItemEntity(Long orderitemId,
                           ItemEntity item,
                           int count,
                           Long itemBuyer,
                           Long itemSeller,
                           OrderEntity order) {
        this.orderitemId = orderitemId;
        this.item = item;
        this.count = count;
        this.order = order;
        this.itemBuyer = itemBuyer;
        this.itemSeller =itemSeller;
    }

    public OrderItemDTO toOrderItemDTO(){
        return OrderItemDTO.builder()
                .item(ItemDTO.toItemDTO(item))
                .itemAmount(this.count)
                .itemBuyer(this.itemBuyer)
                .itemSeller(this.itemSeller)
                .itemPrice(this.item.getPrice())
                .orderitemId(this.orderitemId)
                .itemId(this.item.getItemId())
                .orderDate(this.order==null ? null:this.order.getRegTime())
                .build();
    }

    public static OrderItemEntity setOrderItem(ItemEntity item,
                                               Long itemBuyer,
                                               Long itemSeller,
                                               int count){
        return OrderItemEntity.builder()
                .item(item)
                .itemBuyer(itemBuyer)
                .itemSeller(itemSeller)
                .count(count)
                .build();
    }

}
