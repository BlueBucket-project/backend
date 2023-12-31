package com.example.shopping.entity.item;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.Item.UpdateItemDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
import com.example.shopping.entity.board.BoardEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "item")
@Table
@Getter
@NoArgsConstructor
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
    @Column(name = "item_sell_status")
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

    @Column(name = "item_place")
    private String itemPlace;

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
                      String itemPlace,
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

    public static ItemEntity fromUpdateItemDTO(Long sellerId, ItemDTO item){
        return ItemEntity.builder()
                .itemSeller(sellerId)
                .itemName(item.getItemName())
                .itemDetail(item.getItemDetail())
                .itemSellStatus(item.getItemSellStatus())
                .stockNumber(item.getStockNumber())
                .price(item.getPrice())
                .itemPlace(item.getSellPlace())
                .itemReserver(item.getItemReserver())
                .itemRamount(item.getItemRamount())
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

    public void addItemImgList(ItemImgEntity itemImg){
        this.itemImgList.add(itemImg);
    }

    public void deleteItemImgList(ItemImgEntity itemImg){
        int idx=0;

        // 기존의 이미지리스트와 받아온 itemImgId와 비교해서
        // 동일하면 break로 빠져나와주고
        // 아니라면 +1을 해준다. 그리고 그것들을 리스트에서 삭제
        for(ItemImgEntity item:this.itemImgList){
            if(item.getItemImgId() == itemImg.getItemImgId()){
                break;
            }
            idx +=1;
        }
        this.itemImgList.remove(idx);
    }
}
