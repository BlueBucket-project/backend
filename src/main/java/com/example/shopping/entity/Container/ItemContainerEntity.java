package com.example.shopping.entity.Container;

import com.example.shopping.entity.Base.BaseEntity;
import com.example.shopping.entity.item.ItemEntity;
import lombok.*;

import javax.persistence.*;
/*
 *   writer : 유요한
 *   work :
 *          어느 창고에 어떤 상품들이 있는지 기록하기 위한 엔티티
 *   date : 2024/01/08
 * */
@Entity(name = "container")
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemContainerEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "container_id")
    private Long id;
    private String containerName;
    private String containerAddr;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;


    @Builder
    public ItemContainerEntity(Long id,
                               String containerName,
                               String containerAddr,
                               ItemEntity item) {
        this.id = id;
        this.containerName = containerName;
        this.containerAddr = containerAddr;
        this.item = item;
    }

    public static ItemContainerEntity saveContainer(ItemEntity item) {
        return ItemContainerEntity.builder()
                .containerName(item.getItemPlace().getContainerName())
                .containerAddr(item.getItemPlace().getContainerAddr())
                .item(item)
                .build();
    }
}
