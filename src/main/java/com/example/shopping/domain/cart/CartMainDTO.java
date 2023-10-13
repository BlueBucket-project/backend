package com.example.shopping.domain.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;

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

    public void addCount(int cnt){
        this.count += cnt;
    }
}
