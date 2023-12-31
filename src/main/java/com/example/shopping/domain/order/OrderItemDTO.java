package com.example.shopping.domain.order;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.entity.order.OrderEntity;
import com.example.shopping.entity.order.OrderItemEntity;
import com.example.shopping.repository.item.ItemRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.*;

@ToString
@Getter
@NoArgsConstructor
public class OrderItemDTO {

    @Schema(description = "주문결제일자")
    private LocalDateTime orderDate;
    @Schema(description = "구매상품내역고유번호")
    private Long orderitemId;
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

    public interface orderItemId{
        Long getItemId();
    }

    @Builder
    public OrderItemDTO(Long orderitemId, int itemPrice, Integer itemAmount, Long itemBuyer, Long itemSeller, Long itemId, LocalDateTime orderDate, ItemDTO item) {
        this.orderitemId = orderitemId;
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
                .orderitemId(this.orderitemId)
                .item(this.item.toEntity())
                .build();
    }

    public OrderItemEntity toOrderEntity(OrderEntity order) {
        return OrderItemEntity.builder()
                .count(this.itemAmount)
                .itemBuyer(this.itemBuyer)
                .itemSeller(this.itemSeller)
                .orderitemId(this.orderitemId)
                .item(this.item.toEntity())
                .order(order)
                .build();
    }
}
