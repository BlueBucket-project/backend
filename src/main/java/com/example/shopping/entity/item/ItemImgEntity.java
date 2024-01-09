package com.example.shopping.entity.item;

import com.example.shopping.domain.Item.ItemImgDTO;
import com.example.shopping.entity.Base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.*;
/*
 *   writer : 유요한
 *   work :
 *          상품 이미지에 대한 엔티티
 *   date : 2023/10/22
 * */
@Entity(name = "item_img")
@Table
@ToString(exclude = "item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImgEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_img_id")
    private Long itemImgId;
    @Column(name = "upload_img_path")
    private String uploadImgPath;
    @Column(name = "upload_img_name")
    private String uploadImgName;               // 이미지 파일명
    @Column(name = "ori_img_name")
    private String oriImgName;                  // 원본 이미지 파일명
    @Column(name = "upload_img_url")
    private String uploadImgUrl;                // 이미지 조회 경로
    @Column(name = "rep_img_yn")
    private String repImgYn;                    // 대표 이미지 여부 Y면 대표이미지를 보여줌

    // 다대일 연관관계
    // @JoinColumn(name = "item_id") 이렇게 하는 거는 여기가 주인이라
    // 표시해주는 것이다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Builder
    public ItemImgEntity(Long itemImgId,
                         String uploadImgPath,
                         String uploadImgName,
                         String oriImgName,
                         String uploadImgUrl,
                         String repImgYn,
                         ItemEntity item) {
        this.itemImgId = itemImgId;
        this.uploadImgPath = uploadImgPath;
        this.uploadImgName = uploadImgName;
        this.oriImgName = oriImgName;
        this.uploadImgUrl = uploadImgUrl;
        this.repImgYn = repImgYn;
        this.item = item;
    }

    public void changeRepImgY(){
        this.repImgYn = "Y";
    }
    public void changeRepImgN(){
        this.repImgYn = "N";
    }
    public static ItemImgEntity toEntity(List<ItemImgDTO> productImg, ItemEntity itemEntity) {
        ItemImgEntity itemImg = null;
        for (int i = 0; i < productImg.size(); i++) {
            ItemImgDTO itemImgDTO = productImg.get(i);
            itemImg = ItemImgEntity.builder()
                    .oriImgName(itemImgDTO.getOriImgName())
                    .uploadImgPath(itemImgDTO.getUploadImgPath())
                    .uploadImgUrl(itemImgDTO.getUploadImgUrl())
                    .uploadImgName(itemImgDTO.getUploadImgName())
                    .item(itemEntity)
                    .repImgYn(i == 0 ? "Y" : "N")
                    .build();
        }
        return itemImg;
    }

}
