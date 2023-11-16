package com.example.shopping.domain.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;

@Getter
@ToString
@NoArgsConstructor
public class OrderMainDTO {
    private Long itemId;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    private int count;

    @Schema(description = "구매자 아이디")
    private Long itemReserver;

    @Builder
    public OrderMainDTO(Long itemId, int count, Long itemReserver) {
        this.itemId = itemId;
        this.count = count;
        this.itemReserver = itemReserver;
    }
}
