package com.example.shopping.domain.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
/*
 *   writer : 유요한,오현진
 *   work :
 *          관리자가 처리하기 위한 request DTO
 *   date : 2023/11/27
 * */
@Getter
@ToString
@NoArgsConstructor
public class OrderMainDTO {
    private Long itemId;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    private int count;

    @Schema(description = "구매자 email")
    private String itemReserver;

    @Builder
    public OrderMainDTO(Long itemId, int count, String itemReserver) {
        this.itemId = itemId;
        this.count = count;
        this.itemReserver = itemReserver;
    }
}
