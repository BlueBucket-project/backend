package com.example.shopping.entity.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
/*
 *   writer : 오현진
 *   work :
 *          주문 엔티티
 *          여기에는 주문번호, 주문받은 관리자, 주문한 유저, 그리고 주문상품이 있습니다.
 *   date : 2023/10/22
 * */
@Table(name = "orders")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long orderId;

    @Column(name="order_admin")
    private Long orderAdmin;

    @Column(name="order_member")
    private Long orderMember;

    @OneToMany(mappedBy="order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItem = new ArrayList<>();

    @Builder
    public OrderEntity(Long orderId,
                       Long orderAdmin,
                       Long orderMember,
                       List<OrderItemEntity> orderItem) {
        this.orderId = orderId;
        this.orderAdmin = orderAdmin;
        this.orderMember = orderMember;
        this.orderItem = orderItem == null ? new ArrayList<>() : orderItem;
    }

    public OrderDTO toDTO(){
        return OrderDTO.builder()
                .orderDate(this.getRegTime())
                .orderId(this.orderId)
                .orderAdmin(this.orderAdmin)
                .orderMember(this.orderMember)
                .orderItem(this.orderItem.stream()
                        .map(OrderItemEntity::toOrderItemDTO).collect(Collectors.toList()))
                .build();
    }
}
