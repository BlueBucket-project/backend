package com.example.shopping.service.admin;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.board.BoardSecret;
import com.example.shopping.domain.board.ReplyStatus;
import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.member.*;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.domain.order.OrderMainDTO;
import com.example.shopping.entity.Container.ContainerEntity;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.item.ItemException;
import com.example.shopping.exception.member.UserException;
import com.example.shopping.exception.service.OutOfStockException;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.cart.CartItemRepository;
import com.example.shopping.repository.cart.CartRepository;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.repository.order.OrderItemRepository;
import com.example.shopping.repository.order.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemImgRepository itemImgRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private AdminServiceImpl adminService;


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

    ContainerEntity container = ContainerEntity.builder()
            .containerName("1지점")
            .containerAddr("서울시 고척동 130-44")
            .build();

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

    private BoardEntity createBoard() {
        return BoardEntity.builder()
                .boardSecret(BoardSecret.UN_LOCK)
                .boardId(1L)
                .title("제목")
                .content("내용")
                .member(createMember())
                .commentEntityList(new ArrayList<>())
                .replyStatus(ReplyStatus.REPLY_X)
                .item(createItem())
                .build();
    }

    MemberEntity member = MemberEntity.builder()
            .memberId(1L)
            .memberName("테스트유저")
            .memberPw("test123!@#")
            .email("test123@test.com")
            .nickName("유저1")
            .memberRole(Role.USER)
            .memberPoint(0)
            .build();

    MemberEntity member2 = MemberEntity.builder()
            .memberId(2L)
            .memberName("판매자")
            .memberPw("seller123!@#")
            .email("seller123@test.com")
            .nickName("판매자")
            .memberRole(Role.USER)
            .memberPoint(0)
            .build();

    MemberEntity admin = MemberEntity.builder()
            .memberId(3L)
            .memberName("관리자")
            .memberPw("admin123!@#")
            .email("admin123@test.com")
            .nickName("관리자")
            .memberRole(Role.ADMIN)
            .build();

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

    ItemEntity reservedItem1 = ItemEntity.builder()
            .itemId(1L)
            .itemName("테스트")
            .itemDetail("Test Detail")
            .itemSellStatus(ItemSellStatus.RESERVED)
            .itemReserver(member2.getEmail())
            .itemRamount(1)
            .itemPlace(container)
            .price(10000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();
    ItemEntity selledItem1 = ItemEntity.builder()
            .itemId(1L)
            .itemName("테스트")
            .itemDetail("Test Detail")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace(container)
            .price(10000)
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
            .itemPlace(container)
            .price(30000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .boardEntityList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemEntity reservedItem2 = ItemEntity.builder()
            .itemId(2L)
            .itemName("스프링")
            .itemDetail("스프링 테스트")
            .itemSellStatus(ItemSellStatus.RESERVED)
            .itemReserver(member2.getEmail())
            .itemRamount(1)
            .itemPlace(container)
            .price(30000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .boardEntityList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemEntity reservedOverStockItem2 = ItemEntity.builder()
            .itemId(2L)
            .itemName("스프링")
            .itemDetail("스프링 테스트")
            .itemSellStatus(ItemSellStatus.RESERVED)
            .itemReserver(member2.getEmail())
            .itemRamount(5)
            .itemPlace(container)
            .price(30000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .boardEntityList(new ArrayList<>())
            .itemSeller(1L)
            .build();

    ItemEntity selledItem2 = ItemEntity.builder()
            .itemId(2L)
            .itemName("스프링")
            .itemDetail("스프링 테스트")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace(container)
            .price(30000)
            .stockNumber(2)
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


    @Test
    @DisplayName("관리자 회원가입 서비스 테스트")
    void 관리자_회원가입() {
        // given
        MemberEntity member = createMember();
        RequestMemberDTO memberDTO = RequestMemberDTO.builder()
                .email(createMember().getEmail())
                .memberName(createMember().getMemberName())
                .nickName(createMember().getNickName())
                .memberPw(createMember().getMemberPw())
                .memberRole(createMember().getMemberRole())
                .build();
        given(memberRepository.findByNickName(anyString())).willReturn(member);
        given(memberRepository.save(any())).willReturn(member);

        // when
        adminService.adminSignUp(memberDTO);

        // then
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("관리자 서비스 구매확정 테스트")
    void 예약상품_주문() {
        OrderMainDTO order = OrderMainDTO.builder()
                .itemId(1L)
                .count(1)
                .itemReserver(member2.getEmail())
                .build();
        OrderMainDTO order2 = OrderMainDTO.builder()
                .itemId(2L)
                .count(1)
                .itemReserver(member2.getEmail())
                .build();
        OrderItemDTO orderItem1 = OrderItemDTO.builder()
                .orderItemId(1L)
                .orderDate(LocalDateTime.now())
                .itemBuyer(member2.getMemberId())
                .itemAmount(1)
                .itemPrice(10000)
                .itemSeller(member.getMemberId())
                .item(savedItemDTO1)
                .build();
        OrderItemDTO orderItem2 = OrderItemDTO.builder()
                .orderItemId(2L)
                .orderDate(LocalDateTime.now())
                .itemBuyer(member2.getMemberId())
                .itemAmount(1)
                .itemPrice(30000)
                .itemSeller(member.getMemberId())
                .item(savedItemDTO2)
                .build();
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        OrderDTO savedOrder = OrderDTO.builder()
                .orderId(1L)
                .orderDate(LocalDateTime.now())
                .orderAdmin(admin.getMemberId())
                .orderMember(member2.getMemberId())
                .orderItem(orderItems)
                .build();

        List<OrderMainDTO> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);

        List<ItemImgEntity> imgEntityList = new ArrayList<>();
        ItemImgEntity img = ItemImgEntity.builder()
                .itemImgId(1L)
                .build();
        imgEntityList.add(img);

        given(memberRepository.findByEmail(admin.getEmail())).willReturn(admin);
        given(memberRepository.findByEmail(member2.getEmail())).willReturn(member2);
        given(memberRepository.save(any())).willReturn(member2);

        given(itemRepository.findByItemId(1L)).willReturn(reservedItem1);
        given(itemRepository.findByItemId(2L)).willReturn(reservedItem2);

        given(orderRepository.save(any())).willReturn(savedOrder);

        given(orderItemRepository.save(orderItem1, savedOrder)).willReturn(orderItem1);
        given(orderItemRepository.save(orderItem2, savedOrder)).willReturn(orderItem2);

        given(itemRepository.findById(orderItem1.getItemId())).willReturn(Optional.ofNullable(reservedItem1));
        given(itemRepository.save(any())).willReturn(selledItem1);

        given(itemImgRepository.findByItemItemId(anyLong())).willReturn(imgEntityList);
        doNothing().when(itemImgRepository).deleteById(anyLong());

        given(memberRepository.findByEmail(member2.getEmail())).willReturn(member2);
        given(itemRepository.findById(orderItem2.getItemId())).willReturn(Optional.ofNullable(reservedItem2));
        given(itemRepository.save(any())).willReturn(selledItem2);

        //구매자 장바구니
        CartDTO cart = CartDTO.builder()
                .cartId(1L)
                .member(InfoMemberDTO.toInfoMember(ResponseMemberDTO.toMemberDTO(member)))
                .build();

        //기존 장바구니 물품 예시들
        CartItemDTO cItem = CartItemDTO.builder()
                .cart(cart)
                .cartItemId(1L)
                .mbrId(1L)
                .price(10000)
                .item(ItemDTO.toItemDTO(createItem()))
                .count(1)
                .build();

        given(cartRepository.findByMemberMemberId(anyLong())).willReturn(cart);
        given(cartItemRepository.findByCartItemDTO(anyLong(), anyLong())).willReturn(cItem);
        given(cartItemRepository.save(any())).willReturn(cItem);

        OrderDTO finalOrder = adminService.orderItem(orders, admin.getEmail());

        Assertions.assertThat(finalOrder.getOrderId()).isEqualTo(savedOrder.getOrderId());
        Assertions.assertThat(finalOrder.getOrderAdmin()).isEqualTo(savedOrder.getOrderAdmin());
        Assertions.assertThat(finalOrder.getOrderItem().get(0).getItemId()).isEqualTo(savedOrder.getOrderItem().get(0).getItemId());
        Assertions.assertThat(selledItem1.getStockNumber()).isEqualTo(2);
        Assertions.assertThat(selledItem2.getStockNumber()).isEqualTo(2);

    }

    @Test
    void 예약상품_주문_예외처리_예약물품아님() throws Exception {
        OrderMainDTO order = OrderMainDTO.builder()
                .itemId(1L)
                .count(1)
                .itemReserver(member2.getEmail())
                .build();
        OrderMainDTO order2 = OrderMainDTO.builder()
                .itemId(2L)
                .count(1)
                .itemReserver(member2.getEmail())
                .build();

        List<OrderMainDTO> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);

        given(memberRepository.findById(member2.getMemberId())).willReturn(Optional.ofNullable(member2));
        given(memberRepository.findByEmail(admin.getEmail())).willReturn(admin);

        given(itemRepository.findByItemId(1L)).willReturn(reservedItem1);
        given(itemRepository.findByItemId(2L)).willReturn(savedItem2);

        assertThrows(ItemException.class, () -> adminService.orderItem(orders, admin.getEmail()));

    }

    @Test
    void 예약상품_주문_예외처리_재고부족() throws Exception {
        OrderMainDTO order = OrderMainDTO.builder()
                .itemId(1L)
                .count(1)
                .itemReserver(member2.getEmail())
                .build();
        OrderMainDTO order2 = OrderMainDTO.builder()
                .itemId(2L)
                .count(5)
                .itemReserver(member2.getEmail())
                .build();

        List<OrderMainDTO> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);

        given(memberRepository.findById(member2.getMemberId())).willReturn(Optional.ofNullable(member2));
        given(memberRepository.findByEmail(admin.getEmail())).willReturn(admin);

        given(itemRepository.findByItemId(1L)).willReturn(reservedItem1);
        given(itemRepository.findByItemId(2L)).willReturn(reservedOverStockItem2);

        assertThrows(OutOfStockException.class, () -> adminService.orderItem(orders, admin.getEmail()));

    }
}
