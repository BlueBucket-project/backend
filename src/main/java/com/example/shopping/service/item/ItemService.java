package com.example.shopping.service.item;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemImgDTO;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.service.s3.S3ItemImgUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Log4j2
public class ItemService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final S3ItemImgUploaderService s3ItemImgUploaderService;

    // 상품 등록 메소드
    public ResponseEntity<?> saveItem(ItemDTO itemDTO,
                                      List<MultipartFile> itemFiles,
                                      String memberEmail) throws Exception {
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);

        if(findUser != null) {
            // 상품 등록
            ItemEntity item = ItemEntity.builder()
                    .itemName(itemDTO.getItemName())
                    .itemDetail(itemDTO.getItemDetail())
                    .itemSellStatus(itemDTO.getItemSellStatus())
                    .stockNumber(itemDTO.getStockNumber())
                    .price(itemDTO.getPrice())
                    .itemPlace(itemDTO.getSellPlace())
                    .build();

            // S3에 업로드
            List<ItemImgDTO> productImg = s3ItemImgUploaderService.upload("product", itemFiles);
            List<ItemImgEntity> itemImgEntities = new ArrayList<>();

            for (int i = 0; i < productImg.size(); i++) {
                ItemImgDTO itemImgDTO = productImg.get(i);
                ItemImgEntity imgEntity = ItemImgEntity.builder()
                        .oriImgName(itemImgDTO.getOriImgName())
                        .uploadImgPath(itemImgDTO.getUploadImgPath())
                        .uploadImgUrl(itemImgDTO.getUploadImgUrl())
                        .uploadImgName(itemImgDTO.getUploadImgName())
                        .item(item)
                        .repImgYn(i == 0 ? "Y" : "N")
                        .build();
                ItemImgEntity saveImg = itemImgRepository.save(imgEntity);
                log.info("img : " + saveImg);
                itemImgEntities.add(saveImg);
            }
            item = ItemEntity.builder()
                    .itemName(itemDTO.getItemName())
                    .itemDetail(itemDTO.getItemDetail())
                    .itemSellStatus(itemDTO.getItemSellStatus())
                    .stockNumber(itemDTO.getStockNumber())
                    .price(itemDTO.getPrice())
                    .itemPlace(itemDTO.getSellPlace())
                    .itemImgList(itemImgEntities)
                    .build();

            ItemEntity saveItem = itemRepository.save(item);
            ItemDTO toItemDTO = ItemDTO.toItemDTO(saveItem);
            return ResponseEntity.ok().body(toItemDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
        }
    }

}
