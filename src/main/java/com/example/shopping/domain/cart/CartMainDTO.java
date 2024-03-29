package com.example.shopping.domain.cart;

import com.example.shopping.entity.cart.CartItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
/*
 *   writer : 오현진
 *   work :
 *          장바구니에 담을 때 사용하는 request DTO
 *   date : 2023/11/16
 * */
@ToString
@Getter
@NoArgsConstructor
public class CartMainDTO {

    @Schema(description = "상품번호")
    private Long itemId;

    @Schema(description = "상품개수")
    @Min(value = 1, message = "최소 1개 이상 담아주세요")
    private int count;

    @Builder
    public CartMainDTO(Long itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }

    public static CartMainDTO toMainDTO(CartItemEntity cartItem){
        return CartMainDTO.builder()
                .itemId(cartItem.getItem().getItemId())
                .count(cartItem.getCount())
                .build();
    }
}
