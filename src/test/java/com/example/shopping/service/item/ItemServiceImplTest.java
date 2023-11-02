package com.example.shopping.service.item;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.Item.UpdateItemDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.member.UserException;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemImgRepository itemImgRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    MemberEntity member = MemberEntity.builder()
            .memberId(1L)
            .memberName("테스트유저")
            .memberPw("test123!@#")
            .email("test123@test.com")
            .nickName("유저1")
            .memberRole(Role.USER)
            .build();

    MemberEntity member2 = MemberEntity.builder()
            .memberId(2L)
            .memberName("판매자")
            .memberPw("seller123!@#")
            .email("seller123@test.com")
            .nickName("판매자")
            .memberRole(Role.USER)
            .build();
    ItemDTO newItem1 = ItemDTO.builder()
            .itemId(1L)
            .itemName("테스트")
            .itemDetail("Test Detail")
            .sellPlace("서울시 관악구")
            .price(10000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemDTO newItem2 = ItemDTO.builder()
            .itemId(2L)
            .itemName("스프링")
            .itemDetail("스프링 테스트")
            .sellPlace("서울시 강남구")
            .price(30000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemDTO newItem3 = ItemDTO.builder()
            .itemId(3L)
            .itemName("마지막")
            .itemDetail("상품 테스트")
            .sellPlace("서울시 중구")
            .price(30000)
            .stockNumber(1)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemEntity savedItem1 = ItemEntity.builder()
            .itemId(1L)
            .itemName("테스트")
            .itemDetail("Test Detail")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace("서울시 관악구")
            .price(10000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemEntity editedItem1 = ItemEntity.builder()
            .itemId(1L)
            .itemName("테스트")
            .itemDetail("Test Detail - Edit")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace("서울시 관악구")
            .price(20000)
            .stockNumber(2)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemDTO savedItemDTO1 = ItemDTO.builder()
            .itemId(1L)
            .itemName("테스트")
            .itemDetail("Test Detail")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .memberNickName("유저1")
            .sellPlace("서울시 관악구")
            .price(10000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();
    ItemEntity savedItem2 = ItemEntity.builder()
            .itemId(2L)
            .itemName("스프링")
            .itemDetail("스프링 테스트")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace("서울시 강남구")
            .price(30000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .boardEntityList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemDTO savedItemDTO2 = ItemDTO.builder()
            .itemId(2L)
            .itemName("스프링")
            .itemDetail("스프링 테스트")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .memberNickName("유저1")
            .sellPlace("서울시 강남구")
            .price(30000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemEntity savedItem3 = ItemEntity.builder()
            .itemId(3L)
            .itemName("마지막")
            .itemDetail("상품 테스트")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace("서울시 중구")
            .price(30000)
            .stockNumber(1)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemDTO savedItemDTO3 = ItemDTO.builder()
            .itemId(3L)
            .itemName("마지막")
            .itemDetail("상품 테스트")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .memberNickName("유저1")
            .sellPlace("서울시 중구")
            .price(30000)
            .stockNumber(1)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    @Test
    void 상품등록() throws Exception {

        given(memberRepository.findByEmail(member.getEmail())).willReturn(member);
        given(itemRepository.save(any())).willReturn(savedItem1);

        ItemDTO item = itemService.saveItem(newItem1, new ArrayList<>(), member.getEmail());

        Assertions.assertThat(item.getItemName()).isEqualTo(savedItemDTO1.getItemName());
        Assertions.assertThat(item.getMemberNickName()).isEqualTo(savedItemDTO1.getMemberNickName());
        Assertions.assertThat(item.getItemImgList().size()).isEqualTo(savedItemDTO1.getItemImgList().size());
    }

    @Test
    void 상품수정() throws Exception {

        UpdateItemDTO updateItem = UpdateItemDTO.builder()
                .itemName("테스트")
                .itemDetail("Test Detail - Edit")
                .sellPlace("서울시 관악구")
                .price(20000)
                .stockNumber(2)
                .build();

        given(itemRepository.findById(1L)).willReturn(Optional.ofNullable(savedItem1));
        given(memberRepository.findByEmail(member.getEmail())).willReturn(member);
        given(itemImgRepository.findByItemItemId(1L)).willReturn(new ArrayList<>());
        given(itemRepository.save(any())).willReturn(editedItem1);

        ItemDTO item = itemService.updateItem(1L, updateItem, new ArrayList<>(), member.getEmail());

        Assertions.assertThat(item.getItemName()).isEqualTo("테스트");
        Assertions.assertThat(item.getStockNumber()).isEqualTo(2);
        Assertions.assertThat(item.getItemDetail()).isEqualTo("Test Detail - Edit");
        Assertions.assertThat(item.getPrice()).isEqualTo(20000);
    }

    @Test
    void 상품수정_본인아닐때_Exception() throws Exception {

        UpdateItemDTO updateItem = UpdateItemDTO.builder()
                .itemName("테스트")
                .itemDetail("Test Detail - Edit")
                .sellPlace("서울시 관악구")
                .price(20000)
                .stockNumber(2)
                .build();

        given(itemRepository.findById(1L)).willReturn(Optional.ofNullable(savedItem1));
        given(memberRepository.findByEmail(any())).willReturn(member2);

        org.junit.jupiter.api.Assertions.assertThrows(UserException.class, () -> itemService.updateItem(1L, updateItem, new ArrayList<>(), "test123@test.com")) ;
    }

    private Pageable createPageRequestUsing(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    @Test
    void 상품_세부조회(){
        given(itemRepository.findById(savedItem2.getItemId())).willReturn(Optional.ofNullable(savedItem2));
        Pageable pageable = PageRequest.of(0, 10);
        given(memberRepository.findById(1L)).willReturn(Optional.ofNullable(member));
        ItemDTO item = itemService.getItem(2L, pageable, member.getEmail());

        Assertions.assertThat(item.getItemId()).isEqualTo(2L);
        Assertions.assertThat(item.getItemName()).isEqualTo("스프링");
        Assertions.assertThat(item.getSellPlace()).isEqualTo("서울시 강남구");
        Assertions.assertThat(item.getPrice()).isEqualTo(30000);
    }

    @Test
    void 상품전체조회(){
        List<ItemEntity> itemList = new ArrayList<>();
        itemList.add(savedItem1);
        itemList.add(savedItem2);
        itemList.add(savedItem3);

        Pageable pageable = PageRequest.of(0, 3);
        Pageable pageRequest = createPageRequestUsing(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), itemList.size());

        List<ItemDTO> subItems = itemList.stream().map(ItemDTO::toItemDTO).collect(Collectors.toList()).subList(start, end);
        Page<ItemDTO> items = new PageImpl<>(subItems, pageRequest, itemList.size());

        given(itemRepository.findAll(pageable)).willReturn(items.map(ItemDTO::toEntity));
        given(memberRepository.findById(savedItem1.getItemSeller())).willReturn(Optional.ofNullable(member));

        Page<ItemDTO> itemPages = itemService.getItems(pageable);

        Assertions.assertThat(itemPages.getContent().size()).isEqualTo(3);
    }
}
