package com.example.shopping.domain.order;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.entity.order.OrderEntity;
import com.example.shopping.entity.order.OrderItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/*
 *   writer : 오현진
 *   work :
 *          주문 상품에 대한 정보를 담은 DTO
 *   date : 2023/10/12
 * */
@ToString
@Getter
@NoArgsConstructor
public class OrderItemDTO {

    @Schema(description = "주문결제일자")
    private LocalDateTime orderDate;
    @Schema(description = "구매상품내역고유번호")
    private Long orderItemId;
    @Schema(description = "구매상품가격")
    private int itemPrice;
    @Schema(description = "구매상품수량")
    private Integer itemAmount;
    @Schema(description = "구매자")
    private Long itemBuyer;
    @Schema(description = "판매자")
    private Long itemSeller;
    @Schema(description = "상품번호")
    private Long itemId;
    @Schema(description = "상품번호")
    private ItemDTO item;
    
    @Builder
    public OrderItemDTO(Long orderItemId,
                        int itemPrice, 
                        Integer itemAmount,
                        Long itemBuyer,
                        Long itemSeller,
                        Long itemId,
                        LocalDateTime orderDate,
                        ItemDTO item) {
        this.orderItemId = orderItemId;
        this.itemPrice = itemPrice;
        this.itemAmount = itemAmount;
        this.itemBuyer = itemBuyer;
        this.itemSeller = itemSeller;
        this.itemId = itemId;
        this.orderDate = orderDate;
        this.item = item;
    }

    public OrderItemEntity toEntity() {
        return OrderItemEntity.builder()
                .count(this.itemAmount)
                .itemBuyer(this.itemBuyer)
                .itemSeller(this.itemSeller)
                .orderItemId(this.orderItemId)
                .item(this.item.toEntity())
                .build();
    }

    public OrderItemEntity toOrderEntity(OrderEntity order) {
        return OrderItemEntity.builder()
                .count(this.itemAmount)
                .itemBuyer(this.itemBuyer)
                .itemSeller(this.itemSeller)
                .orderItemId(this.orderItemId)
                .item(this.item.toEntity())
                .order(order)
                .build();
    }
}
