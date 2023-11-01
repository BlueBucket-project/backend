package com.example.shopping.service.order;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.member.Role;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.repository.order.OrderItemRepository;
import com.example.shopping.repository.order.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

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
    MemberEntity admin = MemberEntity.builder()
            .memberId(3L)
            .memberName("관리자")
            .memberPw("admin123!@#")
            .email("admin123@test.com")
            .nickName("관리자")
            .memberRole(Role.ADMIN)
            .build();

    ItemEntity newItem1 = ItemEntity.builder()
            .itemId(1L)
            .itemName("테스트")
            .itemDetail("Test Detail")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace("서울시 관악구")
            .price(10000)
            .stockNumber(3)
            .itemImgList(null)
            .boardEntityList(new ArrayList<>())
            .member(member)
            .build();

    ItemEntity newItem2 = ItemEntity.builder()
            .itemId(2L)
            .itemName("스프링")
            .itemDetail("스프링 테스트")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace("서울시 강남구")
            .price(30000)
            .stockNumber(3)
            .itemImgList(null)
            .member(member)
            .boardEntityList(new ArrayList<>())
            .build();

    ItemEntity newItem3 = ItemEntity.builder()
            .itemId(3L)
            .itemName("마지막")
            .itemDetail("상품 테스트")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace("서울시 중구")
            .price(30000)
            .stockNumber(1)
            .itemImgList(null)
            .member(member)
            .boardEntityList(new ArrayList<>())
            .build();

    ItemEntity newItem4 = ItemEntity.builder()
            .itemId(4L)
            .itemName("상품등록")
            .itemDetail("상품등록 테스트")
            .itemSellStatus(ItemSellStatus.SELL)
            .itemReserver("")
            .itemRamount(0)
            .itemPlace("서울시 중구")
            .price(30000)
            .stockNumber(1)
            .itemImgList(null)
            .member(member2)
            .boardEntityList(new ArrayList<>())
            .build();

    OrderItemDTO orderItem1 = OrderItemDTO.builder()
            .orderDate(LocalDateTime.now())
            .itemId(1L)
            .itemBuyer(1L)
            .itemSeller(2L)
            .orderitemId(1L)
            .itemPrice(10000)
            .itemAmount(1)
            .item(ItemDTO.toItemDTO(newItem1))
            .build();

    OrderItemDTO orderItem2 = OrderItemDTO.builder()
            .orderDate(LocalDateTime.now())
            .itemId(2L)
            .itemBuyer(1L)
            .itemSeller(2L)
            .orderitemId(1L)
            .itemPrice(60000)
            .itemAmount(2)
            .item(ItemDTO.toItemDTO(newItem2))
            .build();

    OrderItemDTO orderItem3 = OrderItemDTO.builder()
            .orderDate(LocalDateTime.now())
            .itemId(3L)
            .itemBuyer(1L)
            .itemSeller(2L)
            .orderitemId(1L)
            .itemPrice(30000)
            .itemAmount(1)
            .item(ItemDTO.toItemDTO(newItem3))
            .build();

    OrderItemDTO orderItem4 = OrderItemDTO.builder()
            .orderDate(LocalDateTime.now())
            .itemId(4L)
            .itemBuyer(2L)
            .itemSeller(1L)
            .orderitemId(1L)
            .itemPrice(30000)
            .itemAmount(1)
            .item(ItemDTO.toItemDTO(newItem4))
            .build();

    private List<OrderItemDTO> orderItemList = new ArrayList<>();
    private List<OrderItemDTO> sellItemList = new ArrayList<>();

    @Test
    void 회원_주문내역조회_관리자(){
        orderItemList.add(orderItem1);
        orderItemList.add(orderItem2);
        orderItemList.add(orderItem3);

        OrderDTO order = OrderDTO.builder()
                .orderId(1L)
                .orderDate(LocalDateTime.now())
                .orderMember(1L)
                .orderItem(orderItemList)
                .orderAdmin(3L)
                .build();
        List<OrderDTO> allOrder = new ArrayList<>();
        allOrder.add(order);

        given(memberRepository.findByEmail(any())).willReturn(admin);
        given(orderRepository.findByOrderAdmin(admin.getMemberId())).willReturn(allOrder);
        given(orderItemRepository.findByOrderOrderId(1L)).willReturn(orderItemList);

        Pageable pageable = PageRequest.of(0, 3);
        Page<OrderItemDTO> orderItemDTOPage = orderService.getOrdersPage(pageable, admin.getEmail());

        Assertions.assertThat(orderItemDTOPage.getSize()).isEqualTo(3);
    }

    @Test
    void 회원_주문내역조회_구매자이자_판매자(){
        orderItemList.add(orderItem1);
        orderItemList.add(orderItem2);

        OrderDTO order = OrderDTO.builder()
                .orderId(1L)
                .orderDate(LocalDateTime.now())
                .orderMember(1L)
                .orderItem(orderItemList)
                .orderAdmin(3L)
                .build();
        List<OrderDTO> allOrder = new ArrayList<>();
        allOrder.add(order);
        sellItemList.add(orderItem4);

        given(memberRepository.findByEmail(any())).willReturn(member);
        given(orderRepository.findByOrderMember(member.getMemberId())).willReturn(allOrder);
        given(orderItemRepository.findByOrderOrderId(1L)).willReturn(orderItemList);

        given(orderItemRepository.findByItemSeller(member.getMemberId())).willReturn(sellItemList);

        Pageable pageable = PageRequest.of(0, 3);
        Page<OrderItemDTO> orderItemDTOPage = orderService.getOrdersPage(pageable, member.getEmail());

        Assertions.assertThat(orderItemDTOPage.getSize()).isEqualTo(3);
    }
}
