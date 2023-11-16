package com.example.shopping.service.cart;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.cart.*;
import com.example.shopping.entity.cart.CartItemEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.cart.CartException;
import com.example.shopping.exception.service.OutOfStockException;
import com.example.shopping.repository.cart.CartItemRepository;
import com.example.shopping.repository.cart.CartRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public CartDTO addCart(CartMainDTO cartItem, String mbrEmail) {

        // 장바구니에 최종 저장된 아이템정보
        CartDTO savedCartItem = new CartDTO();

        MemberEntity member = memberRepository.findByEmail(mbrEmail);
        ItemEntity item = itemRepository.findById(cartItem.getItemId()).orElseThrow(()->new CartException("해당 상품이 존재하지 않습니다."));

        //기존 장바구니 여부 확인
        CartDTO cart = cartRepository.findByMemberMemberId(member.getMemberId());

        if (cart != null) {
            log.info("기존 장바구니 여부 확인 : "+ cart.toString());
            //장바구니 아이템 체크
            CartMainDTO savedCart = cartItemRepository.findByCartMainDTO(cart.getCartId(), cartItem.getItemId());
            CartItemDTO itemDetail = new CartItemDTO();

            //기존에 있는 아이템 추가 시
            if (savedCart != null) {
                //수량증가
                itemDetail = cartItemRepository.findByCartItemDTO(cart.getCartId(), savedCart.getItemId());

                checkItemStock(cartItem.getItemId(), itemDetail.getCount() + cartItem.getCount());
                itemDetail.modifyCount(itemDetail.getCount() + cartItem.getCount());
                log.info("기존 상품 - 수량 증가여부 확인 : "+ itemDetail.getCount());

                cart.updateCartItems(itemDetail);
                savedCartItem = cartRepository.save(cart);
            } else {
                //아니라면 CartItem Insert
                itemDetail = CartItemDTO.setCartItem(cart, item, cartItem.getCount());
                cart.addCartItems(itemDetail);
                savedCartItem = cartRepository.save(cart);
            }
        }
        //기존에 생성된 장바구니 없다면 생성
        else {
            log.info("기존 장바구니 없음");
            CartDTO newCart = new CartDTO();
            newCart = newCart.createCart(member);
            log.info("새로운 장바구니 생성 : " + newCart.toString());
//            CartItemDTO itemDetail = CartItemDTO.builder()
//                    .price(item.getPrice() * cartItem.getCount())
//                    .count(cartItem.getCount())
//                    .cart(newCart)
//                    .item(ItemDTO.toItemDTO(item))
//                    .mbrId(member.getMemberId())
//                    .build();
            CartItemDTO itemDetail = CartItemDTO.setCartItem(newCart, item, cartItem.getCount());
            newCart.addCartItems(itemDetail);

            savedCartItem = cartRepository.create(newCart);
        }

        return savedCartItem;
    }

    @Override
    @Transactional
    public String deleteCart(List<CartUpdateDTO> cartItems, String mbrEmail){

        try{
            List<Long> cartItemId = new ArrayList<>();
            // 장바구니 상품 삭제하기
            for(CartUpdateDTO item : cartItems){
                cartItemId.add(cartItemRepository.findByCartItemDTO(item.getCartId(), item.getItemId()).getCartItemId());
            }
            for(Long id : cartItemId){
                cartItemRepository.delete(id);
            }
        }
        catch(NullPointerException e){
            throw new CartException("삭제하려고 하는 상품id나 카트번호가 잘못 되었습니다.");
        }
        catch (Exception e){
            throw new CartException("장바구니에서 상품을 삭제하는데 실패하였습니다.\n" + e.getMessage());
        }

        return "장바구니에서 상품을 삭제하였습니다.";
    }

    @Override
    @Transactional
    public String updateCart(CartUpdateDTO updateItem, String email) {

        checkItemStock(updateItem.getItemId(), updateItem.getCount());

        try{
            CartItemDTO cartItem = cartItemRepository.findByCartItemDTO(updateItem.getCartId(), updateItem.getItemId());

            cartItem.modifyCount(updateItem.getCount());
            cartItemRepository.save(cartItem);
        }
        catch (Exception e){
            throw new CartException("상품 수량 수정에 실패하였습니다.");
        }

        return "상품 수량 수정에 성공하였습니다.";
    }

    //장바구니 상품 구매예약
    @Override
    @Transactional
    public String orderCart(List<CartOrderDTO> cartOrderList, String email) {

        try{
            for(CartOrderDTO cartOrder : cartOrderList){
                CartItemDTO cartItem = cartItemRepository.findByCartItemId(cartOrder.getCartItemId());

                checkItemStock(cartItem.getItem().getItemId(), cartItem.getCount());

                ItemEntity updateItem = itemRepository.findById(cartItem.getItem().getItemId()).orElseThrow();
                if(updateItem.getItemSellStatus() == ItemSellStatus.RESERVED)
                    throw new CartException("상품("+ updateItem.getItemId()+")은 이미 예약되어 있습니다.\n예약자 : " +updateItem.getItemReserver());

                updateItem.changeStatus(ItemSellStatus.RESERVED);
                updateItem.reserveItem(email, cartItem.getCount());
            }
            return "구매예약에 성공하였습니다.";
        }
        catch (NullPointerException | NoSuchElementException e){
            throw new CartException("예약하려고 하는 장바구니상품id가 해당 회원의 장바구니 상품이 아닙니다.");
        }
        catch(CartException e){
            throw e;
        }
        catch(Exception e){
            throw new CartException("장바구니에서 상품을 예약하는데 실패하였습니다.\n" + e.getMessage());
        }

    }

    //구매예약상품 취소
    @Override
    @Transactional
    public String cancelCartOrder(List<CartOrderDTO> cartOrderList, String email) {
        try{
            for(CartOrderDTO cartOrder : cartOrderList){
                CartItemDTO cartItem = cartItemRepository.findByCartItemId(cartOrder.getCartItemId());

                ItemEntity updateItem = itemRepository.findById(cartItem.getItem().getItemId()).orElseThrow();
                if(updateItem.getItemSellStatus() == ItemSellStatus.SELL)
                    throw new CartException("상품("+ updateItem.getItemId()+")은 이미판매 상태입니다.");
                updateItem.changeStatus(ItemSellStatus.SELL);
                updateItem.reserveItem(null, 0);
            }
            return "구매예약을 취소하였습니다.";
        }
        catch (NullPointerException | NoSuchElementException e){
            throw new CartException("예약취소하려고 하는 장바구니상품id가 해당 회원의 장바구니 상품이 아닙니다.");
        }
        catch(CartException e){
            throw e;
        }
        catch(Exception e){
            throw new CartException("구매예약 취소 작업이 실패하였습니다.\n" + e.getMessage());
        }

    }

    //장바구니 상품 리스트 형태로 조회하기
    @Override
    public List<CartItemDTO> getCartList(String email) {

        List<CartItemDTO> cartItems = new ArrayList<>();

        Long mbrId = memberRepository.findByEmail(email).getMemberId();
        Long cartId = cartRepository.findByMemberMemberId(mbrId).getCartId();

        if(cartId == null)
            throw new CartException("장바구니에 물품이 없습니다.");
        else{
            cartItems = cartItemRepository.findByCartCartId(cartId);
            return cartItems;
        }
    }

    //장바구니 상품 페이지 형태로 조회하기
    @Override
    public Page<CartItemDTO> getCartPage(Pageable pageable, String email) {
        Long mbrId = memberRepository.findByEmail(email).getMemberId();
        Long cartId = 0L;
        try{
            cartId = cartRepository.findByMemberMemberId(mbrId).getCartId();
        }catch (Exception e){
            throw new CartException("장바구니가 없습니다.");
        }

        //Pageable 값 셋팅 - List to Page
        Pageable pageRequest = createPageRequestUsing(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        List<CartItemDTO> cartItems = null;

        if(cartId == null)
            throw new CartException("장바구니에 물품이 없습니다.");
        else{
            cartItems = cartItemRepository.findByCartCartId(cartId);
            for(CartItemDTO cart : cartItems){
                if(cart.getItem().getMemberNickName() == null){
                    cart.getItem().setMemberNickName(Objects.requireNonNull(memberRepository.findById(cart.getItem().getItemSeller()).orElse(null)).getNickName());
                }
            }
            int start = (int) pageRequest.getOffset();
            int end = Math.min((start + pageRequest.getPageSize()), cartItems.size());

            List<CartItemDTO> subCartItems = cartItems.subList(start, end);

            return new PageImpl<>(subCartItems, pageRequest, cartItems.size());
        }
    }

    private Pageable createPageRequestUsing(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    private void checkItemStock(Long itemId, int modifyCnt){
        ItemEntity item = itemRepository.findByItemId(itemId);

        if(item == null){
            throw new OutOfStockException("상품이 존재하지 않습니다.");
        }

        if(item.getItemSellStatus() == ItemSellStatus.SOLD_OUT){
            throw new OutOfStockException("이미 상품이 판매완료되었습니다.");
        }

        if(item.getStockNumber() < modifyCnt){
            throw new OutOfStockException("재고가 부족합니다. 요청수량 : " + modifyCnt +
                    " 재고 : " + item.getStockNumber());
        }
    }
}
