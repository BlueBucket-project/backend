package com.example.shopping.domain.Item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

// 삭제하려고 할때 받아오는 requestDTO
@Getter
@NoArgsConstructor
@ToString
public class DelItemDTO {

    @NotNull(message = "아이템 판매자를 입력해야합니다. 공백으로라도 보내주세요.")
    @Schema(description = "아이템 판매자")
    private Long itemSeller;

    @Builder
    public DelItemDTO(Long itemSeller) {
        this.itemSeller = itemSeller;
    }
}
