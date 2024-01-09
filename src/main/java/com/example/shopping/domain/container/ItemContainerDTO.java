package com.example.shopping.domain.container;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.entity.Container.ItemContainerEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class ItemContainerDTO {
    private String containerName;
    private String containerAddr;
    private ItemDTO itemDTO;

    @Builder
    public ItemContainerDTO(String containerName, String containerAddr, ItemDTO itemDTO) {
        this.containerName = containerName;
        this.containerAddr = containerAddr;
        this.itemDTO = itemDTO;
    }

    public static ItemContainerDTO from(ItemContainerEntity container) {
        return ItemContainerDTO.builder()
                .containerName(container.getContainerName())
                .containerAddr(container.getContainerAddr())
                .itemDTO(ItemDTO.builder()
                        .itemId(container.getItem().getItemId())
                        .itemName(container.getItem().getItemName())
                        .price(container.getItem().getPrice())
                        .itemDetail(container.getItem().getItemDetail())
                        .itemSellStatus(container.getItem().getItemSellStatus())
                        .regTime(container.getItem().getRegTime())
                        .sellPlace(container.getItem().getItemPlace().getContainerName()
                                +"/"+container.getItem().getItemPlace().getContainerAddr())
                        .stockNumber(container.getItem().getStockNumber())
                        .itemReserver(container.getItem().getItemReserver())
                        .itemRamount(container.getItem().getItemRamount())
                        .itemSeller(container.getItem().getItemSeller())
                        .build())
                .build();
    }
}
