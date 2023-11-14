package com.example.shopping.service.admin;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.member.Role;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.domain.order.OrderMainDTO;
import com.example.shopping.entity.item.ItemEntity;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
            .itemPlace("서울시 관악구")
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
            .itemPlace("서울시 관악구")
            .price(10000)
            .stockNumber(3)
            .itemImgList(new ArrayList<>())
            .itemSeller(1L)
            .build();
    ItemEntity selledItem1= ItemEntity.builder()
            .itemId(1L)
            .itemName("테스트")
            .itemDetail("Test Detail")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace("서울시 관악구")
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
            .itemPlace("서울시 강남구")
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
            .itemPlace("서울시 강남구")
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
            .itemPlace("서울시 강남구")
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
            .itemPlace("서울시 강남구")
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
    void 예약상품_주문(){
        OrderMainDTO order = OrderMainDTO.builder()
                .itemId(1L)
                .count(1)
                .itemReserver(member2.getMemberId())
                .build();
        OrderMainDTO order2 = OrderMainDTO.builder()
                .itemId(2L)
                .count(1)
                .itemReserver(member2.getMemberId())
                .build();
        OrderItemDTO orderItem1 = OrderItemDTO.builder()
                .orderitemId(1L)
                .orderDate(LocalDateTime.now())
                .itemBuyer(member2.getMemberId())
                .itemAmount(1)
                .itemPrice(10000)
                .itemSeller(member.getMemberId())
                .item(savedItemDTO1)
            .build();
        OrderItemDTO orderItem2 = OrderItemDTO.builder()
                .orderitemId(2L)
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

        given(memberRepository.findById(member2.getMemberId())).willReturn(Optional.ofNullable(member2));
        given(memberRepository.findByEmail(admin.getEmail())).willReturn(admin);

        given(itemRepository.findByItemId(1L)).willReturn(reservedItem1);
        given(itemRepository.findByItemId(2L)).willReturn(reservedItem2);

        given(orderRepository.save(any())).willReturn(savedOrder);

        given(orderItemRepository.save(orderItem1,savedOrder)).willReturn(orderItem1);
        given(orderItemRepository.save(orderItem2,savedOrder)).willReturn(orderItem2);

        given(memberRepository.findByEmail(member2.getEmail())).willReturn(member2);
        given(itemRepository.findById(orderItem1.getItemId())).willReturn(Optional.ofNullable(reservedItem1));
        given(itemRepository.save(any())).willReturn(selledItem1);

        given(memberRepository.findByEmail(member2.getEmail())).willReturn(member2);
        given(itemRepository.findById(orderItem2.getItemId())).willReturn(Optional.ofNullable(reservedItem2));
        given(itemRepository.save(any())).willReturn(selledItem2);

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
                .itemReserver(member2.getMemberId())
                .build();
        OrderMainDTO order2 = OrderMainDTO.builder()
                .itemId(2L)
                .count(1)
                .itemReserver(member2.getMemberId())
                .build();

        List<OrderMainDTO> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);

        given(memberRepository.findById(member2.getMemberId())).willReturn(Optional.ofNullable(member2));
        given(memberRepository.findByEmail(admin.getEmail())).willReturn(admin);

        given(itemRepository.findByItemId(1L)).willReturn(reservedItem1);
        given(itemRepository.findByItemId(2L)).willReturn(savedItem2);

        org.junit.jupiter.api.Assertions.assertThrows(ItemException.class, () ->adminService.orderItem(orders, admin.getEmail()));

    }

    @Test
    void 예약상품_주문_예외처리_재고부족() throws Exception {
        OrderMainDTO order = OrderMainDTO.builder()
                .itemId(1L)
                .count(1)
                .itemReserver(member2.getMemberId())
                .build();
        OrderMainDTO order2 = OrderMainDTO.builder()
                .itemId(2L)
                .count(5)
                .itemReserver(member2.getMemberId())
                .build();

        List<OrderMainDTO> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);

        given(memberRepository.findById(member2.getMemberId())).willReturn(Optional.ofNullable(member2));
        given(memberRepository.findByEmail(admin.getEmail())).willReturn(admin);

        given(itemRepository.findByItemId(1L)).willReturn(reservedItem1);
        given(itemRepository.findByItemId(2L)).willReturn(reservedOverStockItem2);

        org.junit.jupiter.api.Assertions.assertThrows(OutOfStockException.class, () ->adminService.orderItem(orders, admin.getEmail()));

    }
}
