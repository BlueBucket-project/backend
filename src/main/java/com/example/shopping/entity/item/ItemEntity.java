package com.example.shopping.entity.item;

import com.example.shopping.domain.Item.CreateItemDTO;
import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.Item.UpdateItemDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
import com.example.shopping.entity.Container.ContainerEntity;
import com.example.shopping.entity.board.BoardEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
/*
 *   writer : 유요한, 오현진
 *   work :
 *          상품 엔티티
 *   date : 2024/01/08
 * */
@Entity(name = "item")
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"itemId", "itemName", "price", "stockNumber","itemDetail", "itemSellStatus","itemPlace","itemSeller"})
public class ItemEntity extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name", nullable = false)
    private String itemName;     // 상품 명
    @Column(name = "item_price", nullable = false)
    private int price;          // 가격
    @Column(name = "stock_number",nullable = false)
    private int stockNumber;    // 재고수량

    // BLOB, CLOB 타입 매핑
    // CLOB이란 사이즈가 큰 데이터를 외부 파일로 저장하기 위한 데이터입니다.
    // 문자형 대용량 파일을 저장하는데 사용하는 데이터 타입이라고 생각하면 됩니다.
    // BLOB은 바이너리 데이터를 DB외부에 저장하기 위한 타입입니다.
    // 이미지, 사운드, 비디오 같은 멀티미디어 데이터를 다룰 때 사용할 수 있습니다.
    @Lob
    @Column(nullable = false, name = "item_detail")
    private String itemDetail;  // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    @Column(name = "item_sell_status", nullable = false)
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

    @Embedded
    @Column(name = "item_place", nullable = false)
    private ContainerEntity itemPlace;

    @Column(name="item_reserver")
    private String itemReserver;

    @Column(name="item_ramount")
    private int itemRamount;

    // 여기서 보면 cascade = CascadeType.ALL 이렇게 추가한 거는
    // 상품을 수정, 삭제하면 itemImg도 같이 영향을 가게 하기 위해서이다.
    // item 엔티티에 추가하면 연관관계에 있는 itemImg도 영향이 간다.
    // 보통 상품을 삭제하면 이미지도 같이 삭제되기 때문에 이렇게 했다.
    @Column(name = "item_img")
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("itemImgId asc")
    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgEntity> itemImgList = new ArrayList<>();

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "member_id")
    //private MemberEntity member;
    @Column(name="item_seller")
    private Long itemSeller;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("boardId desc")
    private List<BoardEntity> boardEntityList = new ArrayList<>();

    @Builder
    public ItemEntity(Long itemId,
                      String itemName,
                      int price,
                      int stockNumber,
                      String itemDetail,
                      ItemSellStatus itemSellStatus,
                      List<ItemImgEntity> itemImgList,
                      ContainerEntity itemPlace,
                      String itemReserver,
                      int itemRamount,
                      Long itemSeller,
                      List<BoardEntity> boardEntityList) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
        this.itemImgList = itemImgList==null ? new ArrayList<>():itemImgList;
        this.itemPlace = itemPlace;
        this.itemRamount = itemRamount;
        this.itemReserver = itemReserver;
        this.boardEntityList = boardEntityList;
        this.itemSeller = itemSeller;
    }
    public static ItemEntity saveEntity(ItemDTO itemDTO,
                                        CreateItemDTO saveItem) {
        return ItemEntity.builder()
                .itemName(itemDTO.getItemName())
                .itemDetail(itemDTO.getItemDetail())
                .itemPlace(ContainerEntity.builder()
                        .containerName(saveItem.getSellPlace().getContainerName())
                        .containerAddr(saveItem.getSellPlace().getContainerAddr())
                        .build())
                .itemSellStatus(itemDTO.getItemSellStatus())
                .stockNumber(itemDTO.getStockNumber())
                .price(itemDTO.getPrice())
                .itemSeller(itemDTO.getItemSeller())
                .itemRamount(itemDTO.getItemRamount())
                .itemReserver(itemDTO.getItemReserver() == null ? null : itemDTO.getItemReserver())
                .build();
    }


    public ItemEntity updateItem(UpdateItemDTO item) {
        return ItemEntity.builder()
                .itemId(this.itemId)
                .itemName(item.getItemName())
                .itemDetail(item.getItemDetail())
                .itemPlace(this.itemPlace)
                .itemSellStatus(this.itemSellStatus)
                .stockNumber(item.getStockNumber())
                .price(item.getPrice())
                .itemSeller(item.getItemSeller())
                .itemRamount(this.itemRamount)
                .itemReserver(this.itemReserver == null ? null : this.itemReserver)
                .itemImgList(this.itemImgList)
                .boardEntityList(this.boardEntityList)
                .build();
    }

    // 상품 상태 바꿔주는 메소드
    public void itemSell(int cnt){
        // 아이템 판매 시
        // 재고감소, 상품상태변경, 예약자 및 예약수량 초기화
        this.stockNumber -= cnt;

        if(this.stockNumber == 0){
            this.itemSellStatus = ItemSellStatus.SOLD_OUT;
        }
        else{
            this.itemSellStatus = ItemSellStatus.SELL;
        }

        this.itemReserver = null;
        this.itemRamount = 0;
    }


    // 상태만 바꿔주는 메소드
    public void changeStatus(ItemSellStatus status){
        this.itemSellStatus = status;
    }

    // 상품 구매예약 시 예약정보 셋팅
    public void reserveItem(String itemReserver, int amount){
        this.itemReserver = itemReserver;
        this.itemRamount = amount;
    }

    // ItemEntity에 있는 이미지 리스트에 추가
    public void addItemImgList(ItemImgEntity itemImg){
        this.itemImgList.add(itemImg);
    }


    public void remainImgId(List<Long> remainImgId) {
        for (Long remainImg : remainImgId) {
            // 남겨줄 이미지id와 엔티티안에 있는 이미지 리스트의 id와 비교해준다.
            this.itemImgList.forEach(img -> {
                if(!img.getItemImgId().equals(remainImg)) {
                    // 엔티티 이미지id와 넘겨받은 남겨줄 이미지id가 같지않으면
                    // 기존의 이미지에서 삭제해줍니다.
                    this.itemImgList.remove(img);
                }
            });
        }
    }

}
