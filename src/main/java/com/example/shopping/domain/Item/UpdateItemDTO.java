package com.example.shopping.domain.Item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Getter
@NoArgsConstructor
@ToString
public class UpdateItemDTO {
    @Schema(description = "상품 이름")
    @NotBlank(message = "상품명은 필수 입력입니다.")
    private String itemName;     // 상품 명

    @Schema(description = "상품 가격")
    @NotNull(message = "가격은 필수 입력입니다.")
    private int price;          // 가격

    @Schema(description = "상품 설명")
    @NotNull(message = "설명은 필수 입력입니다.")
    private String itemDetail;  // 상품 상세 설명

    @Schema(description = "상품 재고 수량")
    @NotNull(message = "재고 수량은 필수 입력입니다.")
    private int stockNumber;    // 재고수량

    @Schema(description = "판매지역")
    @NotNull(message = "판매지역을 입력해야 합니다.")
    private String sellPlace;

    @NotNull(message = "남길 이미지를 입력해주셔야 합니다. 공백으로라도 보내주세요.")
    @Schema(description = "남길 이미지id")
    private List<Long> remainImgId;

    @NotNull(message = "아이템 판매자를 입력해야합니다. 공백으로라도 보내주세요.")
    @Schema(description = "아이템 판매자")
    private Long itemSeller;

    @Builder
    public UpdateItemDTO(String itemName,
                         int price,
                         String itemDetail,
                         int stockNumber,
                         String sellPlace,
                         List<Long> delImgId,
                         Long itemSeller) {
        this.itemName = itemName;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stockNumber = stockNumber;
        this.sellPlace = sellPlace;
        this.remainImgId = delImgId;
        this.itemSeller = itemSeller;
    }

    public void setSellPlace(String place) {
        this.sellPlace = place;
    }
}
