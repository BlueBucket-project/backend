package com.example.shopping.entity.item;

import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.entity.Base.BaseTimeEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "item")
@Table
@Getter
@ToString
@NoArgsConstructor
public class ItemEntity extends BaseTimeEntity {
    @Id @GeneratedValue
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
    @Column(name = "item_sell_stauts")
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

    @Column(name = "item_place")
    private String itemPlace;

    // 여기서 보면 cascade = CascadeType.ALL 이렇게 추가한 거는
    // 상품을 수정, 삭제하면 itemImg도 같이 영향을 가게 하기 위해서이다.
    // item 엔티티에 추가하면 연관관계에 있는 itemImg도 영향이 간다.
    // 보통 상품을 삭제하면 이미지도 같이 삭제되기 때문에 이렇게 했다.
    @Column(name = "item_img")
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgEntity> itemImgList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Builder
    public ItemEntity(Long itemId,
                      String itemName,
                      int price,
                      int stockNumber,
                      String itemDetail,
                      ItemSellStatus itemSellStatus,
                      List<ItemImgEntity> itemImgList,
                      String itemPlace,
                      MemberEntity member) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
        this.itemImgList = itemImgList;
        this.itemPlace = itemPlace;
        this.member = member;
    }
}