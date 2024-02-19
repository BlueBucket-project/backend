package com.example.shopping.service.item;

import com.example.shopping.domain.Item.*;
import com.example.shopping.domain.container.ContainerDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.Container.ContainerEntity;
import com.example.shopping.entity.Container.ItemContainerEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.member.UserException;
import com.example.shopping.repository.container.ItemContainerRepository;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemQuerydslRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ItemContainerRepository itemContainerRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemImgRepository itemImgRepository;
    @Mock
    private ItemQuerydslRepository itemQuerydslRepository;

    @InjectMocks
    private ItemServiceImpl itemService;


    ContainerEntity container = ContainerEntity.builder()
            .containerName("1지점")
            .containerAddr("서울시 고척동 130-44")
            .build();


    private MemberEntity createMember() {
        MemberEntity member = MemberEntity.builder()
                .memberId(1L)
                .memberPw("dudtjq8990!")
                .memberName("테스터")
                .memberRole(Role.ADMIN)
                .nickName("테스터")
                .email("test@test.com")
                .memberPoint(0)
                .provider(null)
                .providerId(null)
                .address(AddressEntity.builder()
                        .memberAddr("서울시 강남구")
                        .memberZipCode("103-332")
                        .memberAddrDetail("102")
                        .build())
                .build();

        given(memberRepository.findByEmail(anyString())).willReturn(member);
        return member;
    }

    private ItemEntity createItem() {
        return ItemEntity.builder()
                .itemId(1L)
                .itemName("맥북")
                .itemDetail("M3입니다.")
                .itemSeller(1L)
                .itemRamount(0)
                .itemReserver(null)
                .itemImgList(new ArrayList<>())
                .boardEntityList(new ArrayList<>())
                .itemPlace(container)
                .price(1000000)
                .stockNumber(1)
                .build();
    }

    private ItemContainerEntity createContainer() {
        ItemContainerEntity itemContainer = ItemContainerEntity.builder()
                .id(1L)
                .containerName("1지점")
                .containerAddr("서울시 고척동 130-44")
                .item(createItem())
                .build();

        lenient().when(itemContainerRepository.save(any())).thenReturn(itemContainer);
        return itemContainer;
    }


    ItemEntity savedItem1 = ItemEntity.builder()
            .itemId(1L)
            .itemName("테스트")
            .itemDetail("Test Detail")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace(container)
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
            .itemPlace(container)
            .price(30000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .boardEntityList(new ArrayList<>())
            .itemSeller(1L)
            .build();


    ItemEntity savedItem3 = ItemEntity.builder()
            .itemId(3L)
            .itemName("마지막")
            .itemDetail("상품 테스트")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace(container)
            .price(30000)
            .stockNumber(1)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();


    @Test
    @DisplayName("상품 작성 서비스 테스트")
    void 상품등록() throws Exception {
        // given
        ItemEntity item = createItem();
        CreateItemDTO createItemDTO = CreateItemDTO.builder()
                .itemName("상품 이름")
                .itemDetail("상품 내용")
                .sellPlace(ContainerDTO.changeDTO(container))
                .price(10000)
                .stockNumber(5)
                .build();

        ItemImgEntity itemImg = ItemImgEntity.builder()
                .itemImgId(1L)
                .item(item)
                .build();

        item.addItemImgList(itemImg);

        MemberEntity member = createMember();
        createContainer();

        given(itemRepository.save(any())).willReturn(item);

        // when
        itemService.saveItem(createItemDTO, new ArrayList<>(), member.getEmail());

        // then
        verify(itemRepository).save(any());
    }

    @Test
    @DisplayName("상품 수정 서비스 테스트")
    void 상품수정() throws Exception {
        // given
        ItemEntity item = createItem();
        ItemImgEntity itemImg = ItemImgEntity.builder()
                .itemImgId(1L)
                .item(item)
                .build();
        // 이미지 생성 후 상품 엔티티에 추가
        item.addItemImgList(itemImg);

        // 이미지 리스트 생성
        List<ItemImgEntity> imgEntityList = new ArrayList<>();
        imgEntityList.add(itemImg);

        // 남겨줄 이미지 id 리스트
        List<Long> remainId = new ArrayList<>();
        remainId.add(1L);

        UpdateItemDTO updateItem = UpdateItemDTO.builder()
                .itemName("테스트")
                .itemDetail("Test Detail - Edit")
                .price(20000)
                .stockNumber(2)
                .itemSeller(1L)
                .remainImgId(remainId)
                .build();

        ItemDTO inputItem = ItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(updateItem.getItemName())
                .itemDetail(updateItem.getItemDetail())
                .price(updateItem.getPrice())
                .stockNumber(updateItem.getStockNumber())
                .itemSeller(updateItem.getItemSeller())
                .itemRamount(item.getItemRamount())
                .itemReserver(item.getItemReserver())
                .itemImgList(new ArrayList<>())
                .sellPlace(container.getContainerName() +"/"+ container.getContainerAddr())
                .boardDTOList(new ArrayList<>())
                .build();

        ItemEntity itemEntity = inputItem.toEntity();

        MemberEntity member = createMember();
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(item));
        given(itemRepository.save(any())).willReturn(itemEntity);

        given(itemImgRepository.findByItemItemId(anyLong())).willReturn(imgEntityList);

        UserDetails userDetails = User.withUsername(member.getEmail())
                .password(member.getMemberPw())
                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                .build();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        // when
        ItemDTO result = itemService.updateItem(1L, updateItem, new ArrayList<>(), member.getEmail(), role);

        // then
        Assertions.assertThat(result.getItemName()).isEqualTo("테스트");
        Assertions.assertThat(result.getStockNumber()).isEqualTo(2);
        Assertions.assertThat(result.getItemDetail()).isEqualTo("Test Detail - Edit");
        Assertions.assertThat(result.getPrice()).isEqualTo(20000);
    }


    @Test
    @DisplayName("상품 단건 조회 서비스 테스트")
    void 상품_세부조회(){
        // given
        ItemEntity item = createItem();
        given(itemRepository.findById(anyLong())).willReturn(Optional.ofNullable(item));
        ItemContainerEntity container = createContainer();
        given(itemContainerRepository.findByContainerName(anyString())).willReturn(container);

        // when
        ItemDTO result = itemService.getItem(1L);

        // then
        Assertions.assertThat(result.getItemId()).isEqualTo(Objects.requireNonNull(item).getItemId());
        Assertions.assertThat(result.getItemName()).isEqualTo(item.getItemName());
        Assertions.assertThat(result.getPrice()).isEqualTo(item.getPrice());
    }

    @Test
    @DisplayName("상품 전체 조회 서비스 테스트")
    void 상품전체조회(){
        // given
        List<ItemEntity> itemList = new ArrayList<>();
        itemList.add(savedItem1);
        itemList.add(savedItem2);
        itemList.add(savedItem3);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("itemId").descending());
        Pageable pageRequest = createPageRequestUsing(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), itemList.size());

        List<ItemEntity> subItems = itemList.subList(start, end);
        Page<ItemEntity> items = new PageImpl<>(subItems, pageRequest, itemList.size());

        ItemSearchCondition condition = ItemSearchCondition.builder().build();
        ItemContainerEntity container = createContainer();

        given(itemQuerydslRepository.itemSearch(condition, pageable)).willReturn(items);
        given(itemContainerRepository.findByContainerName(anyString())).willReturn(container);

        Page<ItemDTO> itemPages = itemService.searchItemsConditions(pageable, condition);

        Assertions.assertThat(itemPages.getContent().size()).isEqualTo(itemPages.getContent().size());
    }

    private Pageable createPageRequestUsing(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
