package com.example.shopping.service.item;

import com.example.shopping.domain.Item.*;
import com.example.shopping.domain.board.BoardDTO;
import com.example.shopping.domain.board.BoardSecret;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.item.ItemException;
import com.example.shopping.exception.member.UserException;
import com.example.shopping.repository.cart.CartItemRepository;
import com.example.shopping.repository.item.ItemImgRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class ItemServiceImpl implements ItemService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final S3ItemImgUploaderService s3ItemImgUploaderService;
    private final CartItemRepository cartItemRepository;

    // 상품 등록 메소드
    @Override
    @Transactional
    public ItemDTO saveItem(ItemDTO itemDTO,
                            List<MultipartFile> itemFiles,
                            String memberEmail) throws Exception {
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);

        if (findUser != null) {
            // 상품 등록
            ItemEntity item = ItemEntity.fromUpdateItemDTO(findUser.getMemberId(), itemDTO);

            if (itemFiles.size() != 0) {
                //업로드할 이미지가 없다면 업로드 안하기 위한 로직
                if ((itemFiles.get(0)).getSize() != 0) {
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
            }

            //Cascade특징을 활용하여 ItemRepository.save만 진행해도 ItemImg도 같이 인서트됨
            ItemEntity savedItem = itemRepository.save(item);
            ItemDTO toItemDTO = ItemDTO.toItemDTO(savedItem);
            toItemDTO.setMemberNickName(findUser.getNickName());
            return toItemDTO;
        } else {
            throw new UserException("회원이 없습니다.");
        }
    }

    // 상품 상세정보
    // 상품의 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정합니다.
    // 이럴 경우 JPA가 더티체킹(변경감지)를 수행하지 않아서 성능을 향상 시킬 수 있다.
    @Transactional(readOnly = true)
    @Override
    public ItemDTO getItem(Long itemId) {
        try {
            // 상품 조회
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);

            ItemDTO itemDTO = ItemDTO.toItemDTO(findItem);
            itemDTO.setMemberNickName(
                    memberRepository.findById(itemDTO.getItemSeller())
                            .orElseThrow(EntityNotFoundException::new)
                            .getNickName());
            return itemDTO;

        } catch (EntityNotFoundException e) {
            throw new ItemException("상품이 없습니다. {}, " + e.getMessage());
        }
    }

    // 상품 수정
    @Override
    @Transactional
    public ItemDTO updateItem(Long itemId,
                              UpdateItemDTO itemDTO,
                              List<MultipartFile> itemFiles,
                              String memberEmail,
                              String role) throws Exception {
        try {
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("item : " + findItem);
            MemberEntity findMember = memberRepository.findByEmail(memberEmail);
            log.info("member : " + findMember);
            MemberEntity sellMember = memberRepository.findById(itemDTO.getItemSeller()).orElseThrow();

            // 상품 엔티티에 있는 List로 된 이미지들을 가져오기
            List<ItemImgEntity> itemImgList = findItem.getItemImgList();
            List<BoardEntity> boardEntityList = findItem.getBoardEntityList();

            // 로그인한 본인 그리고 관리자의 경우는 수정가능
            if (findMember.getMemberId().equals(findItem.getItemSeller())
                    || (role.equals("ROLE_ADMIN") && (findItem.getItemSeller().equals(sellMember.getMemberId())))) {
                // 상품 정보 수정
                findItem = ItemEntity.builder()
                        .itemId(findItem.getItemId())
                        .itemName(itemDTO.getItemName())
                        .itemDetail(itemDTO.getItemDetail())
                        .itemPlace(itemDTO.getSellPlace())
                        .itemSellStatus(findItem.getItemSellStatus())
                        .stockNumber(itemDTO.getStockNumber())
                        .price(itemDTO.getPrice())
                        //관리자로 수정작업 시, UpdateItemDTO의 itemSeller
                        //본인이 수정작업 시, userDetails의 email로 id조회
                        .itemSeller(role.equals("ROLE_ADMIN") ? sellMember.getMemberId() : findMember.getMemberId())
                        .itemRamount(findItem.getItemRamount())
                        .itemReserver(findItem.getItemReserver() == null ? null : findItem.getItemReserver())
                        .itemImgList(itemImgList)
                        .boardEntityList(boardEntityList)
                        .build();

                //삭제할 이미지가 있다면 이미지만 삭제 - 삭제를 먼저해야 대표이미지가 없을 때 남은 것 중 대표이미지 셋팅가능
                //삭제 이미지 id받아오기에서 남아있는 이미지 id받아오기로 바뀌어서 해당 작업 추가
                List<ItemImgEntity> itemImgs = itemImgRepository.findByItemItemId(itemId);
                List<Long> itemImgIds = new ArrayList<>();
                if (itemImgs != null && itemImgs.size() != 0) {
                    //기존 이미지들 확인후
                    for (ItemImgEntity imgId : itemImgs) {
                        itemImgIds.add(imgId.getItemImgId());
                    }

                    if (itemDTO.getRemainImgId() != null) {
                        //남기는 이미지 제외 - 삭제할 이미지 아이디 추출과정
                        for (Long imgId : itemDTO.getRemainImgId()) {
                            itemImgIds.remove(imgId);
                        }
                    }
                }
                for (Long imgId : itemImgIds) {
                    ItemImgEntity itemImg = itemImgRepository.findById(imgId).orElseThrow(EntityNotFoundException::new);
                    findItem.deleteItemImgList(itemImg);
                    String result = s3ItemImgUploaderService.deleteFile(itemImg.getUploadImgPath(), itemImg.getUploadImgName());
                }
                if (itemFiles.size() != 0) {
                    //추가 업로드 할 이미지가 있다면 업로드
                    if ((itemFiles.get(0)).getSize() != 0) {
                        List<ItemImgDTO> products = s3ItemImgUploaderService.upload("product", itemFiles);

                        itemImgList = findItem.getItemImgList();

                        //기존 이미지가 없다면 첫 번째 추가 이미지를 대표이미지로 설정
                        if (itemImgList.isEmpty()) {
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
                        else {
                            //기존 이미지 중에 대표이미지여부가 Y인 것이 있는지 확인 후
                            if (itemImgList.stream().filter(img -> img.getRepImgYn().equals("Y")).count() == 0) {
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
                }

                ItemEntity saveItem = itemRepository.save(findItem);
                ItemDTO toItemDTO = ItemDTO.toItemDTO(saveItem);
                toItemDTO.setMemberNickName(role.equals("ROLE_ADMIN") ? sellMember.getNickName() : findMember.getNickName());
                return toItemDTO;

            } else {
                throw new UserException("로그인한 아이디가 상품을 등록한 본인이 아니거나\n관리자로 로그인 하셨을 경우 itemSeller와 상품등록자가 달라 수정작업을 진행할 수 없습니다.");
            }
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new ItemException("상품 수정하는 작업을 실패했습니다.\n" + e.getMessage());
        }
    }

    // 이미지 삭제
    @Override
    @Transactional
    public String removeImg(Long itemId, Long itemImgId, String memberEmail) {
        try {
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
            if (imgEntity.getItem().getItemId().equals(findItem.getItemId())) {
                // SellerId와 MemberId 일치 시 true
                if (findItem.getItemSeller().equals(findUser.getMemberId())) {
                    itemImgRepository.deleteById(imgEntity.getItemImgId());
                    String result = s3ItemImgUploaderService.deleteFile(uploadFilePath, uuidFileName);
                    log.info("result : " + result);

                    // 상품 번호로 담겨져있는 이미지 불러옴
                    List<ItemImgEntity> findItemIdImgList = itemImgRepository.findByItemItemId(itemId);

                    if (!findItemIdImgList.isEmpty()) {
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
                    log.info("item : " + saveItem);
                    return result;
                }
            } else {
                throw new UserException("상품을 등록한 본인이 아닙니다.");
            }
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            throw new ItemException("상품 삭제에 실패했습니다.");
        }

        return "상품삭제에 성공하였습니다.";
    }

    // 상품 삭제
    @Override
    @Transactional
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
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            MemberEntity sellUser = memberRepository.findById(sellerId).orElseThrow();

            if (findUser.getMemberId().equals(findItem.getItemSeller())
                    || (role.equals("ROLE_ADMIN") && sellUser.getMemberId().equals(findItem.getItemSeller()))) {

                //item을 참조하고 있는 자식Entity값 null셋팅
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
            } else {
                throw new UserException("로그인한 아이디가 상품을 등록한 본인이 아니거나\n관리자로 로그인 하셨을 경우 itemSeller와 상품등록자가 달라 삭제작업을 진행할 수 없습니다.");
            }
        } catch (ItemException e) {
            throw e;
        } catch (UserException userException) {
            throw userException;
        } catch (Exception ignored) {
            throw new ItemException("상품 삭제에 실패하였습니다.\n" + ignored.getMessage());
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
        Page<ItemDTO> allItemDTO = allItem.map(ItemDTO::toItemDTO);
        for (ItemDTO item : allItemDTO) {
            item.setMemberNickName(memberRepository.findById(item.getItemSeller()).orElseThrow().getNickName());
        }

        return allItemDTO;
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
    public Page<ItemDTO> searchItemsConditions(Pageable pageable,
                                               String name,
                                               String detail,
                                               Long startP,
                                               Long endP,
                                               String place,
                                               String reserver,
                                               ItemSellStatus status) {
        //Pageable 값 셋팅 - List to Page
        Pageable pageRequest =
                createPageRequestUsing(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        try {
            if (name == null || name.isEmpty()) name = null;
            else name = "%" + name + "%";

            if (detail == null || detail.isEmpty()) detail = null;
            else detail = "%" + detail + "%";

            if (Optional.ofNullable(startP).isEmpty()) startP = null;
            if (Optional.ofNullable(endP).isEmpty()) endP = null;

            if (place == null || place.isEmpty()) place = null;
            else place = "%" + place + "%";

            if (reserver == null || reserver.isEmpty()) reserver = null;
            else reserver = reserver;

            String statusString = "";
            if (status == null) statusString = "%";
            else statusString = status.toString();

            List<ItemDTO> items =
                    itemRepository.findByConditions(name, detail, startP, endP, place, reserver, statusString)
                            .stream()
                            .map(ItemDTO::toItemDTO)
                            .collect(Collectors.toList());

            if (items.isEmpty()) {
                throw new EntityNotFoundException("조건에 만족하는 상품이 없습니다.");
            }

            for (ItemDTO item : items) {
                item.setMemberNickName(memberRepository.findById(
                        item.getItemSeller())
                        .orElseThrow()
                        .getNickName());
            }

            int start = (int) pageRequest.getOffset();
            int end = Math.min((start + pageRequest.getPageSize()), items.size());

            List<ItemDTO> subItems = items.subList(start, end);
            return new PageImpl<>(subItems, pageRequest, items.size());
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EntityNotFoundException("상품 조회에 실패하였습니다.\n" + e.getMessage());
        }
    }

    private Pageable createPageRequestUsing(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
