package com.example.shopping.service.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartMainDTO;
import com.example.shopping.domain.cart.CartUpdateDTO;
import com.example.shopping.entity.cart.CartItemEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.exception.cart.CartException;
import com.example.shopping.exception.service.OutOfStockException;
import com.example.shopping.repository.cart.CartItemRepository;
import com.example.shopping.repository.cart.CartRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final CartItemRepository cartItemRepository;
    @Autowired
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public CartItemDTO addCart(CartMainDTO cartItem, String mbrEmail) {

        // 장바구니에 최종 저장된 아이템정보
        CartItemDTO savedCartItem = new CartItemDTO();

        MemberEntity member = memberRepository.findByEmail(mbrEmail);
        ItemEntity item = itemRepository.findById(cartItem.getItemId()).orElseThrow();

        //기존 장바구니 여부 확인
        CartDTO cart = cartRepository.findByMemberMemberId(member.getMemberId());

        if (cart != null) {
            //장바구니 아이템 체크
            CartMainDTO savedCart = cartItemRepository.findByCartMainDTO(cart.getCartId(), cartItem.getItemId());
            CartItemDTO itemDetail = new CartItemDTO();

            //기존에 있는 아이템 추가 시
            if (savedCart != null) {
                //수량증가
                itemDetail = cartItemRepository.findByCartItemDTO(cart.getCartId(), savedCart.getItemId());
                itemDetail.modifyCount(cartItem.getCount());
                savedCartItem = cartItemRepository.save(itemDetail);
                //savedCart.addCount(cartItem.getCount());
            } else {
                //아니라면 CartItem Insert
                itemDetail = CartItemEntity.setCartItem(cart.toEntity(), item, cartItem.getCount()).toItemDTO();
                savedCartItem = cartItemRepository.save(itemDetail);
            }
        }
        //기존에 생성된 장바구니 없다면 생성
        else {
            CartDTO newCart = new CartDTO();
            CartItemDTO itemDetail = new CartItemDTO();

            newCart = cartRepository.save(newCart.createCart(member));

            //CartItem Insert
            itemDetail = CartItemEntity.setCartItem(newCart.toEntity(), item, cartItem.getCount()).toItemDTO();
            savedCartItem = cartItemRepository.save(itemDetail);
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
        catch (Exception e){
            throw new CartException("장바구니에서 상품을 삭제하는데 실패하였습니다.");
        }

        return "장바구니에서 상품을 삭제하였습니다.";
    }

    @Override
    @Transactional
    public String updateCart(CartUpdateDTO updateItem, String email) {

        ItemEntity item = itemRepository.findByItemId(updateItem.getItemId());
        if(item == null){
            throw new OutOfStockException("재고가 부족합니다. 요청수량 : " + updateItem.getCount() +
                    " 재고 : 0");
        }

        if(item.getStockNumber() < updateItem.getCount()){
            throw new OutOfStockException("재고가 부족합니다. 요청수량 : " + updateItem.getCount() +
                    " 재고 : " + item.getStockNumber());
        }

        try{
            CartItemDTO cartItem = cartItemRepository.findByCartItemDTO(updateItem.getCartId(), updateItem.getItemId());

            cartItem.modifyCount(updateItem.getCount());
            cartItemRepository.save(cartItem);
        }
        catch (Exception e){
            throw new CartException("삼풍 수량 수정에 실패하였습니다.");
        }

        return "상품 수량 수정에 성공하였습니다.";
    }
}
