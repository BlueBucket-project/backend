package com.example.shopping.domain.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Min;

@Getter
public class OrderMainDTO {
    private Long itemId;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    private int count;

    @Schema(description = "구매자 아이디")
    private String itemReserver;

}
