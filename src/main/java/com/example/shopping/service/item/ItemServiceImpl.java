package com.example.shopping.service.item;

import com.example.shopping.domain.Item.*;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.container.ItemContainerDTO;
import com.example.shopping.entity.Container.ContainerEntity;
import com.example.shopping.entity.Container.ItemContainerEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.item.ItemException;
import com.example.shopping.exception.member.UserException;
import com.example.shopping.repository.cart.CartItemRepository;
import com.example.shopping.repository.container.ItemContainerRepository;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemQuerydslRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.service.s3.S3ItemImgUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/*
 *   writer : 유요한, 오현진
 *   work :
 *          상품 서비스
 *          - 상품 CRUD기능과 상품의 판매지역을 가져올 수 있고 조건에 맞춰서 검색할 수 있습니다.
 *          이렇게 인터페이스를 만들고 상속해주는 방식을 선택한 이유는
 *          메소드에 의존하지 않고 필요한 기능만 사용할 수 있게 하고 가독성과 유지보수성을 높이기 위해서 입니다.
 *   date : 2024/01/24
 * */
@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class ItemServiceImpl implements ItemService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final S3ItemImgUploaderService s3ItemImgUploaderService;
    private final CartItemRepository cartItemRepository;
    private final ItemContainerRepository itemContainerRepository;
    private final ItemQuerydslRepository itemQuerydslRepository;

    // 상품 등록 메소드
    @Override
    public ItemDTO saveItem(CreateItemDTO item,
                            List<MultipartFile> itemFiles,
                            String memberEmail) throws Exception {
        // 유저 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);

        if (findUser != null) {
            ItemDTO itemInfo = ItemDTO.builder()
                    .itemName(item.getItemName())
                    .price(item.getPrice())
                    .itemDetail(item.getItemDetail())
                    .stockNumber(item.getStockNumber())
                    .sellPlace(item.getSellPlace().getContainerName()
                            + "/" + item.getSellPlace().getContainerAddr())
                    .itemSellStatus(ItemSellStatus.SELL)
                    .itemReserver(null)
                    .itemRamount(0)
                    .itemSeller(findUser.getMemberId())
                    .build();

            // DTO를 엔티티로 변환
            ItemEntity itemEntity = ItemEntity.saveEntity(itemInfo, item);

            if (!itemFiles.isEmpty()) {
                // S3에 업로드
                List<ItemImgDTO> productImg = s3ItemImgUploaderService.upload("product", itemFiles);
                ItemImgEntity itemImg = ItemImgEntity.toEntity(productImg, itemEntity);
                // ItemEntity에 있는 이미지 리스트에 추가
                itemEntity.addItemImgList(itemImg);
            }
            // container안에 저장해서 테이블로 관리하기 위해서 저장
            ItemContainerEntity saveContainer = ItemContainerEntity.saveContainer(itemEntity);
            itemContainerRepository.save(saveContainer);

            //Cascade특징을 활용하여 ItemRepository.save만 진행해도 ItemImg도 같이 인서트됨
            ItemEntity savedItem = itemRepository.save(itemEntity);
            return ItemDTO.toItemDTO(savedItem);
        } else {
            throw new UserException("회원이 없습니다.");
        }
    }

    // 상품 상세정보
    // 상품의 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정합니다.
    // 이럴 경우 JPA가 더티체킹(변경감지)를 수행하지 않아서 성능을 향상 시킬 수 있다.
    @Transactional(readOnly = true)
    public ItemDTO getItem(Long itemId) {
        try {
            // 상품 조회
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("상품이 없습니다."));

            ItemDTO itemDTO = ItemDTO.toItemDTO(findItem);
            // 상품 컨테이너 조회
            ContainerEntity container =
                    itemContainerRepository.findByContainerName(findItem.getItemPlace().getContainerName());
            if (container == null) {
                itemDTO.setSellPlace("폐점된 지점", null);
            } else {
                itemDTO.setSellPlace(container.getContainerName(), container.getContainerAddr());
            }
            return itemDTO;
        } catch (EntityNotFoundException e) {
            throw new ItemException(e.getMessage());
        }
    }

    // 상품 수정
    @Override
    public ItemDTO updateItem(Long itemId,
                              UpdateItemDTO itemDTO,
                              List<MultipartFile> itemFiles,
                              String memberEmail,
                              String role) {
        try {
            // 상품 조회
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("item : " + findItem);
            // 유저 조회
            MemberEntity findMember = memberRepository.findByEmail(memberEmail);
            log.info("member : " + findMember);
            // 이미지 조회
            List<ItemImgEntity> itemImgs = itemImgRepository.findByItemItemId(itemId);

            if (role.equals("ROLE_ADMIN")) {
                // 상품 정보 수정
                findItem.updateItem(itemDTO);
                // 남겨줄 이미지id를 받지 못한다는 것은 전부 삭제한다는 의미이니
                // 삭제처리
                removeAllImg(itemDTO, itemImgs, findItem);

                // 이미지가 있으면 남겨줄 이미지id를 받고 남겨주고 나머지는 삭제
                // 남겨줄id와 상품 엔티티의 이미지리스트 id를 비교해서 맞지않으면 삭제
                removeImg(itemDTO, itemImgs, findItem);
                // 수정시 추가로 이미지를 넣으려고 하면 s3에 저장하고 엔티티로 변경 후
                // 상품 엔티티의 이미지 리스트에 넣고 썸네일 작업을 해줍니다.
                saveImg(itemFiles, findItem);
                ItemEntity updateItem = itemRepository.save(findItem);
                ItemDTO returnItem = ItemDTO.toItemDTO(updateItem);
                log.info("업데이트 상품 : " + returnItem);
                return returnItem;
            } else {
                throw new UserException("관리자가 아니라 수정작업을 진행할 수 없습니다.");
            }
        } catch (Exception e) {
            throw new ItemException("상품 수정하는 작업을 실패했습니다.\n" + e.getMessage());
        }
    }

    private void removeAllImg(UpdateItemDTO itemDTO, List<ItemImgEntity> itemImgs, ItemEntity findItem) {
        // 남겨줄 이미지가 존재하지 않으면 모두 삭제이니
        if (itemDTO.getRemainImgId().isEmpty()) {
            // 조회한 이미지를 이용해서 s3 삭제
            itemImgs.forEach(itemimg ->
                    s3ItemImgUploaderService.deleteFile(
                            itemimg.getUploadImgPath(),
                            itemimg.getUploadImgName()));
            // 전부 삭제해줄것이니 해당 상품의 이미지 리스트를 삭제
            findItem.getItemImgList().removeAll(itemImgs);
        }
    }

    private void removeImg(UpdateItemDTO itemDTO, List<ItemImgEntity> itemImgs, ItemEntity findItem) {
        // 상품에 이미지가 비어 있지 않으면 true
        if (!itemImgs.isEmpty()) {
            // 남겨줄 이미지id를 넘겨받으면
            if (!itemDTO.getRemainImgId().isEmpty()) {
                // itemDTO.getRemainImgId()에 포함되지 않은
                // 상품 이미지id를 가진 이미지만이 필터링되어 삭제됩니다.
                findItem.getItemImgList().stream()
                        .filter(img -> !itemDTO.getRemainImgId().contains(img.getItemImgId()))
                        .forEach(img -> s3ItemImgUploaderService.deleteFile(img.getUploadImgPath(), img.getUploadImgName()));

                // 넘겨받은 이미지id를 리스트에서 유지하고 나머지를 삭제해줍니다.
                findItem.remainImgId(itemDTO.getRemainImgId());
            }
        }
    }

    private void saveImg(List<MultipartFile> itemFiles, ItemEntity findItem) throws IOException {
        // 추가로 저장할 이미지가 있을 경우
        if (!itemFiles.isEmpty()) {
            // s3에 저장
            List<ItemImgDTO> products = s3ItemImgUploaderService.upload("product", itemFiles);
            // 리스트 이미지 DTO를 리스트 엔티티 이미지로 변경
            ItemImgEntity itemImg = ItemImgEntity.toEntity(products, findItem);
            // 상품의 이미지 리스트에 넣기
            findItem.addItemImgList(itemImg);
            // 추가로 넣은 것과 기존의 것을 합쳤을 때 첫번째인 것을 썸네일로 처리하기 위해서
            // 썸네일 작업
            boolean isFirstImage = true;
            for (ItemImgEntity img : findItem.getItemImgList()) {
                if(isFirstImage) {
                    img.changeRepImgY();
                    isFirstImage = false;
                } else {
                    img.changeRepImgN();
                }
            }
        }
    }

    // 상품 삭제
    @Override
    public String removeItem(Long itemId, Long sellerId, String memberEmail, String role) {

        try {
            // 상품 조회
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ItemException("해당 상품 정보가 존재하지 않습니다."));

            if (findItem.getItemSellStatus() == ItemSellStatus.RESERVED) {
                throw new ItemException("해당 상품은 예약되었으니 관리자 혹은 예약자와 논의 후 삭제를 진행하여 주시기 바랍니다.");
            }

            // 이미지 조회
            List<ItemImgEntity> findImg = itemImgRepository.findByItemItemId(itemId);
            // 회원 조회
            MemberEntity sellUser = memberRepository.findById(sellerId).orElseThrow();

            if (role.equals("ROLE_ADMIN") && sellUser.getMemberId().equals(findItem.getItemSeller())) {

                // item을 참조하고 있는 자식Entity값 null셋팅
                List<CartItemDTO> items = cartItemRepository.findByItemId(itemId);

                for (CartItemDTO item : items) {
                    item.setItem(null);
                    cartItemRepository.save(item);
                }
                // 상품 정보 삭제
                itemRepository.delete(findItem);

                for (ItemImgEntity img : findImg) {
                    String uploadFilePath = img.getUploadImgPath();
                    String uuidFileName = img.getUploadImgName();

                    // S3에서 삭제
                    String result = s3ItemImgUploaderService.deleteFile(uploadFilePath, uuidFileName);
                    log.info(result);
                }
            }
        } catch (Exception ignored) {
            throw new ItemException("상품 삭제에 실패하였습니다.\n" + ignored.getMessage());
        }
        return "상품과 이미지를 삭제했습니다.";
    }



    // 상품의 판매지역을 보여줍니다.
    @Override
    @Transactional(readOnly = true)
    public List<ItemContainerDTO> getSellPlaceList() {
        // 반환값이 컬렉션이기 때문에 .stream().map()을 사용합니다.
        return itemContainerRepository.findAll()
                        .stream()
                        .map(ItemContainerDTO::from)
                        .collect(Collectors.toList());
    }

    // 상품 검색 - 여러 조건으로 검색하기
    @Transactional(readOnly = true)
    @Override
    public Page<ItemDTO> searchItemsConditions(Pageable pageable, ItemSearchCondition condition) {
        try {
            Page<ItemEntity> findItemConditions = itemQuerydslRepository.itemSearch(condition, pageable);
            log.info("items : {}", findItemConditions.getContent());

            if (findItemConditions.isEmpty()) {
                throw new EntityNotFoundException("조건에 만족하는 상품이 없습니다.");
            }

            Page<ItemDTO> pageItem = findItemConditions.map(ItemDTO::toItemDTO);

            pageItem.forEach(status -> {
                String[] split = status.getSellPlace().split("/");
                ContainerEntity container = itemContainerRepository.findByContainerName(split[0]);
                if(container == null) {
                    status.setSellPlace("폐점된 지점", null);
                } else {
                    status.setSellPlace(container.getContainerName(), container.getContainerAddr());
                }
            });
            return pageItem;
        } catch (Exception e) {
            log.error("error : " + e.getMessage());
            throw new EntityNotFoundException("상품 조회에 실패하였습니다.\n" + e.getMessage());
        }
    }

}
