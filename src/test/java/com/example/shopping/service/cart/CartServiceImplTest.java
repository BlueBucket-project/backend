package com.example.shopping.service.cart;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.cart.*;
import com.example.shopping.domain.member.ResponseMemberDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.service.OutOfStockException;
import com.example.shopping.repository.cart.CartItemRepository;
import com.example.shopping.repository.cart.CartRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void mockMemberRepositoryTest(){

        MemberEntity member = MemberEntity.builder()
                .memberName("테스트유저")
                .memberPw("test123!@#")
                .email("test123@test.com")
                .nickName("유저1")
                .memberRole(Role.USER)
                .build();

        List<MemberEntity> members = new ArrayList<>();
        members.add(member);

        given(memberRepository.findAll()).willReturn(members);

        List<MemberEntity> findMembers = memberRepository.findAll();
        System.out.println("findMembers = " + findMembers);

        //then
        Assertions.assertEquals(1, findMembers.size());
        Assertions.assertEquals(member.getMemberName(), findMembers.get(0).getMemberName());
    }
    MemberEntity member = MemberEntity.builder()
            .memberId(1L)
            .memberName("테스트유저")
            .memberPw("test123!@#")
            .email("test123@test.com")
            .nickName("유저1")
            .memberRole(Role.USER)
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
            .build();

    //구매자 장바구니
    CartDTO cart = CartDTO.builder()
            .cartId(1L)
            .member(ResponseMemberDTO.toMemberDTO(member))
            .cartItem(null)
            .build();

    //기존 장바구니 물품 예시들
    CartItemDTO cItem = CartItemDTO.builder()
            .cart(cart)
            .cartItemId(1L)
            .mbrId(1L)
            .price(10000)
            .item(ItemDTO.toItemDTO(newItem1))
            .count(1)
            .build();

    CartItemDTO cItem2 = CartItemDTO.builder()
            .cart(cart)
            .cartItemId(2L)
            .mbrId(1L)
            .price(40000)
            .item(ItemDTO.toItemDTO(newItem2))
            .count(2)
            .build();

    //상품보다 오버수량으로 장바구니 담아둠
    CartItemDTO cItem3 = CartItemDTO.builder()
            .cart(cart)
            .cartItemId(3L)
            .mbrId(1L)
            .price(10000)
            .item(ItemDTO.toItemDTO(newItem3))
            .count(2)
            .build();

    String testEmail = "test123@test.com";

    @Test
    void 카트추가_기존아이템_없을때(){
        CartMainDTO cartMain = CartMainDTO.builder()
                .itemId(1L)
                .count(1)
                .build();

        CartItemDTO cartItem = CartItemDTO.builder()
                .cart(cart)
                .cartItemId(1L)
                .mbrId(1L)
                .price(10000)
                .item(ItemDTO.toItemDTO(newItem1))
                .count(1)
                .build();

        //given
        given(memberRepository.findByEmail(testEmail)).willReturn(member);
        given(itemRepository.findById(1L)).willReturn(Optional.of(newItem1));

        given(cartRepository.findByMemberMemberId(1L)).willReturn(cart);
        given(cartItemRepository.findByCartMainDTO(any(), any())).willReturn(null);
        given(cartItemRepository.save(any())).willReturn(cartItem);

        CartItemDTO addedCartItem = cartService.addCart(cartMain, testEmail);
        assertThat(addedCartItem.getCartItemId()).isEqualTo(1L);
        assertThat(addedCartItem.getItem().getItemName()).isEqualTo("테스트");
    }

    @Test
    void 카트추가_기존아이템_있을때_수량증가(){
        CartMainDTO cartMain = CartMainDTO.builder()
                .itemId(1L)
                .count(2)
                .build();

        //최종 증가수량
        CartItemDTO addCartItem = CartItemDTO.builder()
                .cart(cart)
                .cartItemId(1L)
                .mbrId(1L)
                .price(10000)
                .item(ItemDTO.toItemDTO(newItem1))
                .count(3)
                .build();

        //given
        given(memberRepository.findByEmail(testEmail)).willReturn(member);
        given(itemRepository.findById(1L)).willReturn(Optional.of(newItem1));
        given(itemRepository.findByItemId(1L)).willReturn(newItem1);

        given(cartRepository.findByMemberMemberId(1L)).willReturn(cart);
        given(cartItemRepository.findByCartMainDTO(any(), any())).willReturn(cartMain);

        given(cartItemRepository.findByCartItemDTO(1L, 1L)).willReturn(cItem);
        given(cartItemRepository.save(any())).willReturn(addCartItem);

        //when
        CartItemDTO addedCartItem = cartService.addCart(cartMain, testEmail);

        assertThat(addedCartItem.getCartItemId()).isEqualTo(1L);
        assertThat(addedCartItem.getCount()).isEqualTo(3);
    }

    @Test
    void 카트추가_기존아이템_있을때_수량증가_예외처리(){
        CartMainDTO cartMain = CartMainDTO.builder()
                .itemId(3L)
                .count(2)
                .build();

        //최종 증가수량
        CartItemDTO addCartItem = CartItemDTO.builder()
                .cart(cart)
                .cartItemId(3L)
                .mbrId(1L)
                .price(10000)
                .item(ItemDTO.toItemDTO(newItem3))
                .count(2)
                .build();

        //given
        given(memberRepository.findByEmail(testEmail)).willReturn(member);
        given(itemRepository.findById(3L)).willReturn(Optional.of(newItem3));
        given(itemRepository.findByItemId(3L)).willReturn(newItem3);


        given(cartRepository.findByMemberMemberId(1L)).willReturn(cart);
        given(cartItemRepository.findByCartMainDTO(any(), any())).willReturn(cartMain);

        given(cartItemRepository.findByCartItemDTO(1L, 3L)).willReturn(cItem3);

        Assertions.assertThrows(OutOfStockException.class, () -> cartService.addCart(cartMain, "test123@test.com"));
    }

    @Test
    void 카트아이템_수정(){
        CartUpdateDTO updateCart = CartUpdateDTO.builder()
                .cartId(1L)
                .itemId(1L)
                .count(2)
                .build();

        CartItemDTO modifiedItem = CartItemDTO.builder()
                .cart(cart)
                .cartItemId(1L)
                .mbrId(1L)
                .price(10000)
                .item(ItemDTO.toItemDTO(newItem1))
                .count(2)
                .build();

        given(itemRepository.findByItemId(1L)).willReturn(newItem1);
        given(cartItemRepository.findByCartItemDTO(1L, 1L)).willReturn(cItem);
        given(cartItemRepository.save(any())).willReturn(modifiedItem);

        String res = cartService.updateCart(updateCart, testEmail);

        assertThat(res).isEqualTo("상품 수량 수정에 성공하였습니다.");
    }

    @Test
    void 카트아이템_삭제(){
        List<CartUpdateDTO> delCartItems = new ArrayList<>();
        CartUpdateDTO delCartItem = CartUpdateDTO.builder()
                .cartId(1L)
                .itemId(1L)
                .count(1)
                .build();
        CartUpdateDTO delCartItem2 = CartUpdateDTO.builder()
                .cartId(1L)
                .itemId(2L)
                .count(2)
                .build();
        delCartItems.add(delCartItem);
        delCartItems.add(delCartItem2);

        CartItemDTO getCartItem = CartItemDTO.builder()
                .cart(cart)
                .cartItemId(1L)
                .mbrId(1L)
                .price(10000)
                .item(ItemDTO.toItemDTO(newItem1))
                .count(1)
                .build();
        CartItemDTO getCartItem2 = CartItemDTO.builder()
                .cart(cart)
                .cartItemId(2L)
                .mbrId(1L)
                .price(10000)
                .item(ItemDTO.toItemDTO(newItem2))
                .count(2)
                .build();


        given(cartItemRepository.findByCartItemDTO(1L, 1L)).willReturn(getCartItem);
        given(cartItemRepository.findByCartItemDTO(1L, 2L)).willReturn(getCartItem2);

        String res = cartService.deleteCart(delCartItems, testEmail);

        assertThat(res).isEqualTo("장바구니에서 상품을 삭제하였습니다.");
    }

    @Test
    void 카트_구매예약(){
        List<CartOrderDTO> cartOrderDTOList = new ArrayList<>();
        CartOrderDTO cartOrder1 = CartOrderDTO.builder()
                                                .cartItemId(1L)
                                                .build();
        CartOrderDTO cartOrder2 = CartOrderDTO.builder()
                                                .cartItemId(2L)
                                                .build();
        cartOrderDTOList.add(cartOrder1);
        cartOrderDTOList.add(cartOrder2);

        given(cartItemRepository.findByCartItemId(1L)).willReturn(cItem);
        given(cartItemRepository.findByCartItemId(2L)).willReturn(cItem2);

        given(itemRepository.findByItemId(1L)).willReturn(newItem1);
        given(itemRepository.findByItemId(2L)).willReturn(newItem2);

        given(itemRepository.findById(1L)).willReturn(Optional.ofNullable(newItem1));
        given(itemRepository.findById(2L)).willReturn(Optional.ofNullable(newItem2));

        String res = cartService.orderCart(cartOrderDTOList, testEmail);

        assertThat(res).isEqualTo("구매예약에 성공하였습니다.");

    }

    @Test
    void 카트_구매예약중_재고부족으로_예외처리(){
        List<CartOrderDTO> cartOrderDTOList = new ArrayList<>();
        CartOrderDTO cartOrder1 = CartOrderDTO.builder()
                .cartItemId(1L)
                .build();
        CartOrderDTO cartOrder3 = CartOrderDTO.builder()
                .cartItemId(3L)
                .build();
        cartOrderDTOList.add(cartOrder1);
        cartOrderDTOList.add(cartOrder3);

        given(cartItemRepository.findByCartItemId(1L)).willReturn(cItem);
        given(cartItemRepository.findByCartItemId(3L)).willReturn(cItem3);

        given(itemRepository.findByItemId(1L)).willReturn(newItem1);
        given(itemRepository.findById(1L)).willReturn(Optional.ofNullable(newItem1));

        given(itemRepository.findByItemId(3L)).willReturn(newItem3);
        //given(itemRepository.findById(3L)).willReturn(Optional.ofNullable(newItem3));

        Assertions.assertThrows(OutOfStockException.class, () -> cartService.orderCart(cartOrderDTOList, testEmail));
    }

    @Test
    void 카트_구매예약취소(){
        List<CartOrderDTO> cartOrderList = new ArrayList<>();
        CartOrderDTO cartOrderDTO = CartOrderDTO.builder()
                                                .cartItemId(1L)
                                                .build();
        CartOrderDTO cartOrderDTO2 = CartOrderDTO.builder()
                                                .cartItemId(2L)
                                                .build();
        cartOrderList.add(cartOrderDTO);
        cartOrderList.add(cartOrderDTO2);

        given(cartItemRepository.findByCartItemId(1L)).willReturn(cItem);
        given(cartItemRepository.findByCartItemId(2L)).willReturn(cItem2);

        given(itemRepository.findByItemId(1L)).willReturn(newItem1);
        given(itemRepository.findById(1L)).willReturn(Optional.ofNullable(newItem1));

        given(itemRepository.findByItemId(2L)).willReturn(newItem2);
        given(itemRepository.findById(2L)).willReturn(Optional.ofNullable(newItem2));

        String res = cartService.cancelCartOrder(cartOrderList, testEmail);
    }

    @Test
    void 카트아이템_조회(){

    }
}
