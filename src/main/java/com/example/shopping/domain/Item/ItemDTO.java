package com.example.shopping.domain.Item;

import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.entity.Container.ContainerEntity;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 *   writer : 유요한, 오현진
 *   work :
 *          상품에 대한 정보를 담은 ResponseDTO
 *   date : 2024/02/06
 * */
@ToString
@Getter
@NoArgsConstructor
public class ItemDTO {
    @Schema(description = "상품 번호")
    private Long itemId;

    @Schema(description = "상품 이름")
    @NotBlank(message = "상품명은 필수 입력입니다.")
    private String itemName;     // 상품 명

    @Schema(description = "상품 가격")
    @NotNull(message = "가격은 필수 입력입니다.")
    private int price;          // 가격

    @Schema(description = "상품 설명")
    @NotNull(message = "설명은 필수 입력입니다.")
    private String itemDetail;  // 상품 상세 설명

    @Schema(description = "상품 상태")
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

    @Schema(description = "상품 등록 시간")
    private LocalDateTime regTime;

    @Schema(description = "회원 닉네임")
    @NotNull(message = "닉네임은 필수로 입력해야합니다.")
    private String memberNickName;

    @Schema(description = "상품 재고 수량")
    @NotNull(message = "재고 수량은 필수 입력입니다.")
    private int stockNumber;    // 재고수량

    @Schema(description = "판매지역")
    @NotNull(message = "판매지역을 입력해야 합니다.")
    private String sellPlace;

    @Schema(description = "상품 예약자 이메일")
    private String itemReserver;

    @Schema(description = "예약 수량")
    private int itemRamount;

    @Schema(description = "상품 이미지")
    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgDTO> itemImgList = new ArrayList<>();

    @Schema(description = "상품 판매자 아이디")
    private Long itemSeller;

    @Schema(description = "문의글")
    private List<BoardDTO> boardDTOList = new ArrayList<>();

    @Builder
    public ItemDTO(Long itemId,
                   String itemName,
                   int price,
                   String itemDetail,
                   ItemSellStatus itemSellStatus,
                   LocalDateTime regTime,
                   String memberNickName,
                   int stockNumber,
                   String sellPlace,
                   String itemReserver,
                   int itemRamount,
                   Long itemSeller,
                   List<BoardDTO> boardDTOList,
                   List<ItemImgDTO> itemImgList) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
        this.regTime = regTime;
        this.memberNickName = memberNickName;
        this.stockNumber = stockNumber;
        this.sellPlace = sellPlace;
        this.itemReserver = itemReserver;
        this.itemRamount = itemRamount;
        this.boardDTOList = boardDTOList;
        this.itemImgList = itemImgList;
        this.itemSeller = itemSeller;
    }

    public static ItemDTO toItemDTO(ItemEntity item) {
        // 이미지 처리
        List<ItemImgEntity> itemImgEntities =
                (item.getItemImgList() != null) ? item.getItemImgList() : Collections.emptyList();
        List<ItemImgDTO> itemImgDTOList = itemImgEntities.stream()
                .map(ItemImgDTO::toItemImgDTO)
                .collect(Collectors.toList());

        // 문의글 처리
        List<BoardEntity> boardEntityList =
                item.getBoardEntityList() != null ? item.getBoardEntityList() : Collections.emptyList();
        List<BoardDTO> boardDTOS = boardEntityList.stream()
                .map(BoardDTO::toBoardDTO)
                .collect(Collectors.toList());

        // 상품 정보와 이미지 그리고 문의글을 리턴
        return ItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .stockNumber(item.getStockNumber())
                .itemDetail(item.getItemDetail())
                .itemSellStatus(item.getItemSellStatus())
                .regTime(item.getRegTime())
                .sellPlace(item.getItemPlace().getContainerName() + "/" + item.getItemPlace().getContainerAddr())
                .itemReserver(item.getItemReserver())
                .itemRamount(item.getItemRamount())
                .itemSeller(item.getItemSeller())
                .itemImgList(itemImgDTOList)
                .boardDTOList(boardDTOS)
                .build();
    }

    public void setMemberNickName(String nickName) {
        this.memberNickName = nickName;
    }

    public ItemEntity toEntity() {
        String[] splitPlace = this.sellPlace.split("/");

        return ItemEntity.builder()
                .itemId(this.itemId)
                .itemDetail(this.itemDetail)
                .itemName(this.itemName)
                .itemPlace(ContainerEntity.builder()
                        .containerName(splitPlace[0])
                        .containerAddr(splitPlace[1])
                        .build())
                .itemRamount(this.itemRamount)
                .itemReserver(this.itemReserver)
                .itemSellStatus(this.itemSellStatus)
                .price(this.price)
                .itemSeller(this.itemSeller)
                .stockNumber(this.stockNumber)
                .build();
    }

    public void setSellPlace(String placeName, String placeAddr) {
        this.sellPlace = placeName + "/" + placeAddr;
    }
}
