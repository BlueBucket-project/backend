package com.example.shopping.service.cart;

import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.cart.*;
import com.example.shopping.entity.cart.CartEntity;
import com.example.shopping.entity.cart.CartItemEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.cart.CartException;
import com.example.shopping.exception.item.ItemException;
import com.example.shopping.exception.service.OutOfStockException;
import com.example.shopping.repository.cart.CartItemJpaRepository;
import com.example.shopping.repository.cart.CartItemRepository;
import com.example.shopping.repository.cart.CartJpaRepository;
import com.example.shopping.repository.cart.CartRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/*
 *   writer : 오현진, 유요한
 *   work :
 *          장바구니 서비스
 *          - 장바구니 생성, 삭제, 수정 그리고 주문예약과 취소기능이 있습니다.
 *          이렇게 인터페이스를 만들고 상속해주는 방식을 선택한 이유는
 *          메소드에 의존하지 않고 필요한 기능만 사용할 수 있게 하고 가독성과 유지보수성을 높이기 위해서 입니다.
 *   date : 2024/01/29
 * */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartJpaRepository cartJpaRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public CartDTO addCart(CartMainDTO cartItem, String mbrEmail) {

        // 장바구니에 최종 저장된 아이템정보
        CartDTO savedCartItem = null;

        MemberEntity member = memberRepository.findByEmail(mbrEmail);
        ItemEntity item = itemRepository.findById(cartItem.getItemId())
                .orElseThrow(() -> new CartException("해당 상품이 존재하지 않습니다."));

        // 상품 재고 확인
        checkItemStock(cartItem.getItemId(), cartItem.getCount());

        // 기존 장바구니 여부 확인
        CartDTO cart = cartRepository.findByMemberMemberId(member.getMemberId());

        if (cart != null) {
            log.info("기존 장바구니 여부 확인 : " + cart);
            //장바구니 아이템 체크
            CartMainDTO savedCart =
                    cartItemRepository.findByCartMainDTO(cart.getCartId(), cartItem.getItemId());
            CartItemDTO itemDetail;

            // 기존에 있는 아이템 추가 시
            if (savedCart != null) {
                // 수량증가
                itemDetail = cartItemRepository.findByCartItemDTO(cart.getCartId(), savedCart.getItemId());

                // 예약된 상품을 장바구니에 담을 때
                if (itemDetail.getStatus().equals(CartStatus.RESERVED)) {
                    throw new CartException("이미 예약되어있는 상품을 추가하셨습니다");
                }

                // 상품 재고 확인
                checkItemStock(cartItem.getItemId(), itemDetail.getCount() + cartItem.getCount());
                // 상품 개수 변경
                itemDetail.modifyCount(itemDetail.getCount() + cartItem.getCount());
                log.info("기존 상품 - 수량 증가여부 확인 : " + itemDetail.getCount());

                // 장바구니안에 있는 장바구니아이템에 추가하기 위해서
                // 이거는 연관관계를 맺지 않고 리스트로 처리하는 방법
                cart.updateCartItems(itemDetail);
                savedCartItem = cartRepository.save(cart);
            } else {
                // 아니라면 CartItem Insert
                itemDetail = CartItemDTO.setCartItem(cart, item, cartItem.getCount());
                cart.addCartItems(itemDetail);
                savedCartItem = cartRepository.save(cart);
            }
        }
        //기존에 생성된 장바구니 없다면 생성
        else {
            log.info("기존 장바구니 없음");
            CartDTO newCart = CartDTO.createCart(member);
            log.info("새로운 장바구니 생성 : " + newCart.toString());

            CartItemDTO itemDetail = CartItemDTO.setCartItem(newCart, item, cartItem.getCount());
            newCart.addCartItems(itemDetail);

            savedCartItem = cartRepository.create(newCart);
        }
        return savedCartItem;
    }

    @Override
    public String deleteCart(List<UpdateCartDTO> cartItems, String memberEmail) {
        try {
            // 회원 조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            // 장바구니 조회
            CartDTO findCart = cartRepository.findByMemberMemberId(findUser.getMemberId());
            List<Long> cartItemId = new ArrayList<>();
            // 장바구니 상품 삭제하기
            for (UpdateCartDTO item : cartItems) {
                cartItemId.add(cartItemRepository.findByCartItemDTO(item.getCartId(), item.getItemId())
                        .getCartItemId());
            }
            if (findUser.getMemberId().equals(findCart.getMember().getId())) {
                cartItemId.forEach(cartItemRepository::delete);
            }
        } catch (NullPointerException e) {
            throw new CartException("삭제하려고 하는 상품id나 카트번호가 잘못 되었습니다.");
        } catch (Exception e) {
            throw new CartException("장바구니에서 상품을 삭제하는데 실패하였습니다.\n" + e.getMessage());
        }
        return "장바구니에서 상품을 삭제하였습니다.";
    }

    @Override
    public String updateCart(UpdateCartDTO updateItem, String email) {
        checkItemStock(updateItem.getItemId(), updateItem.getCount());
        try {
            CartItemDTO cartItem = cartItemRepository.findByCartItemDTO(updateItem.getCartId(), updateItem.getItemId());
            cartItem.modifyCount(updateItem.getCount());
            cartItemRepository.save(cartItem);
        } catch (Exception e) {
            throw new CartException("상품 수량 수정에 실패하였습니다.");
        }
        return "상품 수량 수정에 성공하였습니다.";
    }

    //장바구니 상품 구매예약
    @Override
    public ResponseEntity<?> orderCart(List<CartOrderDTO> cartOrderList,
                                    String email) {
        try {
            MemberEntity member = memberRepository.findByEmail(email);
            CartEntity findCart = cartJpaRepository.findByMemberMemberId(member.getMemberId())
                    .orElseThrow(() -> new EntityNotFoundException("장바구니가 존재 하지 않습니다."));

            if (findCart.getMember().getMemberId().equals(member.getMemberId())) {
                List<Long> cartItemIds = cartOrderList.stream()
                        .map(CartOrderDTO::getCartItemId)
                        .collect(Collectors.toList());

                // DB에 JPQL로 직접 업데이트를 직접 처리하고 있습니다.
                // 예약 취소할 id를 받아서 조건에 맞는 데이터를 DB에서 전부 수정해줍니다.
                List<CartItemEntity> cartItems =
                        cartItemJpaRepository.updateCartItemsStatus(CartStatus.RESERVED, cartItemIds);

                for (CartItemEntity cartItem : cartItems) {
                    // 장바구니에서 상품을 예약하려고 하는데 상품 자체가 삭제되어 있으면 동작
                    if (cartItem.getItem() == null) {
                        throw new ItemException("예약 하려고 하는 상품이 판매자에 의해 삭제되었습니다.");
                    }

                    if (cartItem.getItem().getItemSellStatus().equals(ItemSellStatus.SOLD_OUT)) {
                        throw new OutOfStockException("상품이 판매 완료되었습니다.");
                    }

                    if (cartItem.getItem().getItemSellStatus().equals(ItemSellStatus.RESERVED)) {
                        throw new CartException("상품(" + cartItem.getItem().getItemId() + ", " +
                                cartItem.getItem().getItemName() + ")은 이미 예약되어 있습니다." +
                                "\n예약자 : " + cartItem.getItem().getItemReserver());
                    }

                    int itemNumber = cartItem.getItem().getStockNumber();
                    int orderCount = cartItem.getCount();
                    if (itemNumber < orderCount) {
                        throw new OutOfStockException("재고가 부족합니다. \n 요청 수량 : " + itemNumber +
                                "\n재고 : " + orderCount);
                    }
                    // 상품의 예약자와 예약 수량 수정
                    // for문을 돌면서 상태 변화를 추적하는데 이 상태 변화는 커밋전에는 반영되지 않고
                    // 트랜잭션이 커밋될 때 반영되서 하나의 쿼리로 모든 변경 사항이 DB에 적용됩니다.
                    cartItem.orderItem(member.getEmail());
                }
                List<CartItemDTO> cartItemDTOList = cartItems.stream()
                        .map(CartItemDTO::toDTO)
                        .collect(Collectors.toList());
                CartDTO cartDTO = CartDTO.toCartDTO(findCart);
                cartDTO.addList(cartItemDTOList);

                return ResponseEntity.ok().body(cartDTO);
            }
            throw new CartException("예약하려고 하는 장바구니 상품이 해당 회원의 장바구니 상품이 아닙니다.");
        } catch (NullPointerException | NoSuchElementException e) {
            throw new CartException("존재하지 않는 장바구니 상품id입니다.");
        } catch (Exception e) {
            throw new CartException("장바구니에서 상품을 예약하는데 실패하였습니다.\n" + e.getMessage());
        }
    }

    // 구매예약상품 취소
    @Override
    public String cancelCartOrder(List<CartOrderDTO> cartOrderList, String email) {
        try {
            MemberEntity findUser = memberRepository.findByEmail(email);
            CartEntity findCart = cartJpaRepository.findByMemberMemberId(findUser.getMemberId())
                    .orElseThrow(() -> new EntityNotFoundException("장바구니가 존재 하지 않습니다."));

            if (findCart.getMember().getMemberId().equals(findUser.getMemberId())) {
                List<Long> cartItemIds = cartOrderList.stream()
                        .map(CartOrderDTO::getCartItemId)
                        .collect(Collectors.toList());

                List<CartItemEntity> cartItems =
                        // DB에 JPQL로 직접 업데이트를 직접 처리하고 있습니다.
                        // 예약 취소할 id를 받아서 조건에 맞는 데이터를 DB에서 전부 수정해줍니다.
                        cartItemJpaRepository.updateCartItemsStatus(CartStatus.WAIT, cartItemIds);

                cartItems.forEach(cartItem -> {
                    // 장바구니에서 상품을 예약하려고 하는데 상품 자체가 삭제되어 있으면 동작
                    if(cartItem.getItem() == null) {
                        throw new ItemException("예약 하려고 하는 상품이 판매자에 의해 삭제되었습니다.");
                    }
                    cartItem.cancelOrderItem();
                });
                return "구매예약을 취소하였습니다.";
            }
            throw new CartException("예약하려고 하는 장바구니 상품이 해당 회원의 장바구니 상품이 아닙니다.");
        } catch (NullPointerException | NoSuchElementException e) {
            throw new CartException("예약취소하려고 하는 장바구니상품id가 해당 회원의 장바구니 상품이 아닙니다.");
        } catch (CartException e) {
            throw e;
        } catch (Exception e) {
            throw new CartException("구매예약 취소 작업이 실패하였습니다.\n" + e.getMessage());
        }
    }

    // 장바구니 상품 리스트 형태로 조회하기
    @Override
    @Transactional(readOnly = true)
    public List<CartItemDTO> getCartList(String email) {

        List<CartItemDTO> cartItems;

        Long mbrId = memberRepository.findByEmail(email).getMemberId();
        Long cartId = cartRepository.findByMemberMemberId(mbrId).getCartId();

        if (cartId == null)
            throw new CartException("장바구니에 물품이 없습니다.");
        else {
            cartItems = cartItemRepository.findByCartCartId(cartId);
            return cartItems;
        }
    }

    // 장바구니 상품 페이지 형태로 조회하기
    @Override
    @Transactional(readOnly = true)
    public Page<CartItemDTO> getCartPage(Pageable pageable, String email) {
        Long mbrId = memberRepository.findByEmail(email).getMemberId();
        Long cartId = 0L;
        try {
            cartId = cartRepository.findByMemberMemberId(mbrId).getCartId();
        } catch (Exception e) {
            throw new CartException("장바구니가 없습니다.");
        }

        // Pageable 값 셋팅 - List to Page
        Pageable pageRequest = createPageRequestUsing(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        List<CartItemDTO> cartItems = null;

        if (cartId == null)
            throw new CartException("장바구니에 물품이 없습니다.");
        else {
            // PURCHASED 상태를 제외한 cartItem조회
            cartItems = cartItemRepository.findCartItemNotPurchased(cartId);
            for (CartItemDTO cart : cartItems) {
                if (cart.getItem() != null) {
                    if (cart.getItem().getMemberNickName() == null) {
                        cart.getItem().setMemberNickName(Objects.requireNonNull(memberRepository.findById(cart.getItem().getItemSeller()).orElse(null)).getNickName());
                    }
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

    // 상품 재고 확인
    private void checkItemStock(Long itemId, int modifyCnt) {
        ItemEntity item = itemRepository.findByItemId(itemId);

        if (item == null) {
            throw new OutOfStockException("상품이 존재하지 않습니다.");
        }

        if (item.getItemSellStatus() == ItemSellStatus.SOLD_OUT) {
            throw new OutOfStockException("이미 상품이 판매완료되었습니다.");
        }

        if (item.getStockNumber() < modifyCnt) {
            throw new OutOfStockException("재고가 부족합니다. 요청수량 : " + modifyCnt +
                    " 재고 : " + item.getStockNumber());
        }
    }
}
