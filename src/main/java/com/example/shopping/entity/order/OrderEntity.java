package com.example.shopping.entity.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Table(name = "orders")
@Entity
@Getter
@ToString
@NoArgsConstructor
public class OrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long orderId;

    @Column(name="order_admin")
    private Long orderAdmin;

    @Column(name="order_member")
    private Long orderMember;

    @OneToMany(mappedBy="order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItem = new ArrayList<>();

    @Builder

    public OrderEntity(Long orderId, Long orderAdmin, Long orderMember, List<OrderItemEntity> orderItem) {
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

    /*
    public static OrderEntity createOrder(Long orderAdmin, Long orderMember, List<OrderItemEntity> orderItemList) {
        return OrderEntity.builder()
                .orderDate(LocalDateTime.now())
                .orderAdmin(orderAdmin)
                .orderMember(orderMember)
                .orderItem(orderItemList)
                .build();
    }
*/

}
