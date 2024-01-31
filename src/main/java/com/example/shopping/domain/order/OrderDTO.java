package com.example.shopping.domain.order;

import com.example.shopping.entity.order.OrderEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/*
 *   writer : 오현진
 *   work :
 *          주문에 대한 정보를 담은 DTO
 *   date : 2023/12/04
 * */
@ToString
@Getter
@NoArgsConstructor
public class OrderDTO {

    @Schema(description = "주문결제번호")
    private Long orderId;

    private LocalDateTime orderDate;

    @Schema(description = "주문결제자")
    private Long orderAdmin;

    @Schema(description = "구매자아이디")
    private Long orderMember;

    private List<OrderItemDTO> orderItem = new ArrayList<>();

    @Builder
    public OrderDTO(Long orderId,
                    LocalDateTime orderDate,
                    Long orderAdmin,
                    Long orderMember,
                    List<OrderItemDTO> orderItem) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderAdmin = orderAdmin;
        this.orderMember = orderMember;
        this.orderItem = orderItem;
    }

    public static OrderDTO createOrder(Long orderAdmin,
                                       Long orderMember,
                                       List<OrderItemDTO> orderItemList) {
        return OrderDTO.builder()
                .orderAdmin(orderAdmin)
                .orderMember(orderMember)
                .orderItem(orderItemList)
                .build();
    }

    public OrderEntity toEntity(){
        return OrderEntity.builder()
                .orderAdmin(this.orderAdmin)
                .orderMember(this.orderMember)
                .orderId(this.orderId)
                .orderItem(this.orderItem.stream()
                        .map(OrderItemDTO::toEntity).collect(Collectors.toList()))
                .build();
    }

}
