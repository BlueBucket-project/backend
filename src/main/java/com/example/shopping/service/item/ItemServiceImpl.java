package com.example.shopping.service.item;

import com.example.shopping.domain.Item.*;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.item.ItemException;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.service.s3.S3ItemImgUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Log4j2
public class ItemServiceImpl implements ItemService{
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final S3ItemImgUploaderService s3ItemImgUploaderService;

    // 상품 등록 메소드
    @Override
    public ResponseEntity<?> saveItem(ItemDTO itemDTO,
                                      List<MultipartFile> itemFiles,
                                      String memberEmail) throws Exception {
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);
        List<ItemImgEntity> itemImgEntities = new ArrayList<>();

        if(findUser != null) {
            // 상품 등록
            ItemEntity item = ItemEntity.builder()
                    .member(findUser)
                    .itemName(itemDTO.getItemName())
                    .itemDetail(itemDTO.getItemDetail())
                    // 처음 상품을 등록하면 무조건 SELL 상태 - controller에서 셋팅해서 넘겨줌
                    .itemSellStatus(itemDTO.getItemSellStatus())
                    .stockNumber(itemDTO.getStockNumber())
                    .price(itemDTO.getPrice())
                    .itemPlace(itemDTO.getSellPlace())
                    .itemReserver(itemDTO.getItemReserver())
                    .itemRamount(itemDTO.getItemRamount())
                    .build();

            //업로드할 이미지가 없다면 업로드 안하기 위한 로직
            if((itemFiles.get(0)).getSize() != 0){
                // S3에 업로드
                List<ItemImgDTO> productImg = s3ItemImgUploaderService.upload("product", itemFiles);

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

                    item.addItemImgList(imgEntity);
                }
            }
            //Cascade특징을 활용하여 ItemRepository.save만 진행해도 ItemImg도 같이 인서트됨
            ItemEntity savedItem = itemRepository.save(item);
            ItemDTO toItemDTO = ItemDTO.toItemDTO(savedItem);

            return ResponseEntity.ok().body(toItemDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
        }
    }

    // 상품 상세정보
    // 상품의 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정합니다.
    // 이럴 경우 JPA가 더티체킹(변경감지)를 수행하지 않아서 성능을 향상 시킬 수 있다.
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<ItemDTO> getItem(Long itemId) {
        try {
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);

            ItemDTO itemDTO = ItemDTO.toItemDTO(findItem);
            return ResponseEntity.ok().body(itemDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 상품 수정
    @Override
    public ResponseEntity<?> updateItem(Long itemId,
                                        UpdateItemDTO itemDTO,
                                        List<MultipartFile> itemFiles,
                                        String memberEmail) throws Exception {
        try {
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("item : " + findItem);
            MemberEntity findMember = memberRepository.findByEmail(memberEmail);
            log.info("member : " + findMember);
            List<ItemImgEntity> itemImgList = findItem.getItemImgList();

            // 이메일을 userDetails에서 가져와서 조회한 다음
            // 회원 이메일과 상품에 담긴 member 엔티티의 이메일과 비교
            if(findMember.getEmail().equals(findItem.getMember().getEmail())) {
                // 상품 정보 수정
                findItem = ItemEntity.builder()
                        .itemId(findItem.getItemId())
                        .itemName(itemDTO.getItemName())
                        .itemDetail(itemDTO.getItemDetail())
                        .itemPlace(itemDTO.getSellPlace())
                        .itemSellStatus(findItem.getItemSellStatus())
                        .stockNumber(findItem.getStockNumber())
                        .price(itemDTO.getPrice())
                        .member(findMember)
                        .itemRamount(findItem.getItemRamount())
                        .itemReserver(findItem.getItemReserver()==null?null:findItem.getItemReserver())
                        .itemImgList(itemImgList)
                        .build();

                //삭제할 이미지가 있다면 이미지만 삭제 - 삭제를 먼저해야 대표이미지가 없을 때 남은 것 중 대표이미지 셋팅가능
                //삭제 이미지 id받아오기에서 남아있는 이미지 id받아오기로 바뀌어서 해당 작업 추가
                List<ItemImgEntity> itemImgs = itemImgRepository.findByItemItemId(itemId);
                List<Long> itemImgIds = new ArrayList<>();
                if(itemImgs != null){
                    for(ItemImgEntity imgId : itemImgs){
                        itemImgIds.add(imgId.getItemImgId());
                    }
                }

                for(Long imgId : itemDTO.getDelImgId()) {
                    itemImgIds.remove(imgId);
                }

                for(Long imgId : itemImgIds){
                    ItemImgEntity itemImg = itemImgRepository.findById(imgId).orElseThrow(EntityNotFoundException::new);
                    findItem.deleteItemImgList(itemImg);
                    String result = s3ItemImgUploaderService.deleteFile(itemImg.getUploadImgPath(), itemImg.getUploadImgName());
                }

                //추가 업로드 할 이미지가 있다면 업로드
                if((itemFiles.get(0)).getSize() != 0){
                    List<ItemImgDTO> products = s3ItemImgUploaderService.upload("product", itemFiles);

                    itemImgList = findItem.getItemImgList();

                    //기존 이미지가 없다면 첫 번째 추가 이미지를 대표이미지로 설정
                    if(itemImgList.isEmpty()){
                        for (int i = 0; i < products.size(); i++) {
                            ItemImgDTO itemImgDTO = products.get(i);
                            ItemImgEntity imgEntity = ItemImgEntity.builder()
                                    .oriImgName(itemImgDTO.getOriImgName())
                                    .uploadImgName(itemImgDTO.getUploadImgName())
                                    .uploadImgPath(itemImgDTO.getUploadImgPath())
                                    .uploadImgUrl(itemImgDTO.getUploadImgUrl())
                                    .repImgYn(i == 0 ? "Y" : "N")
                                    .item(findItem)
                                    .build();

                            findItem.addItemImgList(imgEntity);
                        }
                    }
                    //기존 이미지가 있다면 추가할 이미지들의 대표이미지여부는 N
                    else{
                        //기존 이미지 중에 대표이미지여부가 Y인 것이 있는지 확인 후
                        if(itemImgList.stream().filter(img->img.getRepImgYn().equals("Y")).count() == 0)
                        {
                            //하나도 없다면 첫번째 이미지의 대표이미지 플래그값 수정
                            itemImgList.get(0).changeRepImgY();
                        }

                        for (int i = 0; i < products.size(); i++) {
                            ItemImgDTO itemImgDTO = products.get(i);
                            ItemImgEntity imgEntity = ItemImgEntity.builder()
                                    .oriImgName(itemImgDTO.getOriImgName())
                                    .uploadImgPath(itemImgDTO.getUploadImgPath())
                                    .uploadImgUrl(itemImgDTO.getUploadImgUrl())
                                    .uploadImgName(itemImgDTO.getUploadImgName())
                                    .repImgYn("N")
                                    .item(findItem)
                                    .build();

                            findItem.addItemImgList(imgEntity);
                        }
                    }
                }
                ItemEntity saveItem = itemRepository.save(findItem);
                ItemDTO toItemDTO = ItemDTO.toItemDTO(saveItem);

                return ResponseEntity.ok().body(toItemDTO);

            } else {
                return ResponseEntity.badRequest().body("이메일이 일치하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 이미지 삭제
    @Override
    public String removeImg(Long itemId, Long itemImgId, String memberEmail) {
        // 이미지 조회
        ItemImgEntity imgEntity = itemImgRepository.findById(itemImgId)
                .orElseThrow(EntityNotFoundException::new);
        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);
        // 상품 조회
        ItemEntity findItem = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        String uploadFilePath = imgEntity.getUploadImgPath();
        String uuidFileName = imgEntity.getUploadImgName();

        // 이미지 엔티티에 있는 Item엔티티에 담겨있는 id와 조회한 Item엔티티 id와 일치하면 true
        if(imgEntity.getItem().getItemId().equals(findItem.getItemId())) {
            // 상품 엔티티에 담아져 있는 유저의 이메일과 조회한 유저의 이메일과 일치하면 true
            if(findItem.getMember().getEmail().equals(findUser.getEmail())) {
                itemImgRepository.deleteById(imgEntity.getItemImgId());
                String result = s3ItemImgUploaderService.deleteFile(uploadFilePath, uuidFileName);
                log.info("result : " + result);

                // 상품 번호로 담겨져있는 이미지 불러옴
                List<ItemImgEntity> findItemIdImgList = itemImgRepository.findByItemItemId(itemId);

                if(!findItemIdImgList.isEmpty()) {
                    for (int i = 0; i < findItemIdImgList.size(); i++) {
                        ItemImgEntity itemImgEntity = findItemIdImgList.get(i);
                        ItemImgEntity itemImg = ItemImgEntity.builder()
                                .itemImgId(itemImgEntity.getItemImgId())
                                .oriImgName(itemImgEntity.getOriImgName())
                                .uploadImgPath(itemImgEntity.getUploadImgPath())
                                .uploadImgUrl(itemImgEntity.getUploadImgUrl())
                                .uploadImgName(itemImgEntity.getUploadImgName())
                                .repImgYn(i == 0 ? "Y" : "N")
                                .item(findItem)
                                .build();
                        ItemImgEntity saveImg = itemImgRepository.save(itemImg);
                        log.info("img : " + saveImg);
                        findItemIdImgList.add(saveImg);
                    }
                } else {
                    // 모든 이미지를 삭제했으므로 상품의 이미지 목록을 비웁니다.
                    findItemIdImgList = new ArrayList<>();
                }
                findItem = ItemEntity.builder()
                        .itemId(findItem.getItemId())
                        .itemName(findItem.getItemName())
                        .itemDetail(findItem.getItemDetail())
                        .itemPlace(findItem.getItemPlace())
                        .stockNumber(findItem.getStockNumber())
                        .price(findItem.getPrice())
                        .itemImgList(findItemIdImgList)
                        .build();
                ItemEntity saveItem = itemRepository.save(findItem);
                log.info("item : " +saveItem);
                return result;
            }
        }
        return "삭제를 실패했습니다.";
    }

    // 상품 삭제
    @Override
    public String removeItem(Long itemId, String memberEmail) {
        // 상품 조회
        ItemEntity findItem = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        // 이미지 조회
        List<ItemImgEntity> findImg = itemImgRepository.findByItemItemId(itemId);
        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);

        if(findUser.getEmail().equals(findItem.getMember().getEmail())) {
            for(ItemImgEntity img : findImg) {
                String uploadFilePath = img.getUploadImgPath();
                String uuidFileName = img.getUploadImgName();

                // 상품 정보 삭제
                itemRepository.delete(findItem);
                //class java.lang.integer cannot be cast to class com.example.shopping.entity.item.itementity Error남
                //itemRepository.deleteByItemId(itemId);
                // DB에서 이미지 삭제
//                itemImgRepository.deleteById(img.getItemImgId());
                // S3에서 삭제
                String result = s3ItemImgUploaderService.deleteFile(uploadFilePath, uuidFileName);
                log.info(result);
            }
        }else  {
            return "해당 유저의 게시글이 아닙니다.";
        }
        return "상품과 이미지를 삭제했습니다.";
    }

    // 전체 상품 보여주기
    @Transactional(readOnly = true)
    @Override
    public Page<ItemDTO> getItems(Pageable pageable) {
        Page<ItemEntity> allItem = itemRepository.findAll(pageable);
        // 각 아이템 엔티티를 ItemDTO로 변환합니다.
        // 이 변환은 ItemDTO::toItemDTO 메서드를 사용하여 수행됩니다.
        return allItem.map(ItemDTO::toItemDTO);
    }

    // 검색
    @Transactional(readOnly = true)
    @Override
    public Page<ItemDTO> getSearchItems(Pageable pageable,
                                        String searchKeyword) {
        Page<ItemEntity> searchItems = itemRepository.findByItemNameContaining(pageable, searchKeyword);
        return searchItems.map(ItemDTO::toItemDTO);
    }

    // 상품 검색 - 여러 조건으로 검색하기
    @Transactional(readOnly = true)
    public Page<ItemDTO> searchItemsConditions(Pageable pageable, String name, String detail, Long startP, Long endP, String place, String reserver, ItemSellStatus status){
        //Pageable 값 셋팅 - List to Page
        Pageable pageRequest = createPageRequestUsing(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        try{

            if (name==null || name.isEmpty()) name = null;
            else name = "%" + name + "%";

            if (detail==null || detail.isEmpty()) detail = null;
            else detail = "%" + detail + "%";

            if (Optional.ofNullable(startP).isEmpty()) startP = null;
            if (Optional.ofNullable(endP).isEmpty()) endP = null;

            if (place==null || place.isEmpty()) place = null;
            else place = "%" + place + "%";

            if (reserver==null || reserver.isEmpty()) reserver = null;
            else reserver = "%" + reserver + "%";

            String statusString = "";
            if(status == null) statusString = "%";
            else statusString = status.toString();

            List<ItemDTO> items = itemRepository.findByConditions(name, detail, startP, endP, place, reserver, statusString).stream().map(ItemDTO::toItemDTO).collect(Collectors.toList());

            if(items.isEmpty()){
                throw new EntityNotFoundException("조건에 만족하는 상품이 없습니다.");
            }

            int start = (int) pageRequest.getOffset();
            int end = Math.min((start + pageRequest.getPageSize()), items.size());

            List<ItemDTO> subItems = items.subList(start, end);
            return new PageImpl<>(subItems, pageRequest, items.size());
        }
        catch (EntityNotFoundException e){
            throw e;
        }
        catch (Exception e){
            throw new EntityNotFoundException("상품 조회에 실패하였습니다.");
        }
    }

    private Pageable createPageRequestUsing(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
