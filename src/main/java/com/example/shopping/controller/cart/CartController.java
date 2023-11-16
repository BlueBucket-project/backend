package com.example.shopping.controller.cart;

import com.example.shopping.domain.cart.*;
import com.example.shopping.exception.cart.CartException;
import com.example.shopping.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니담기", description = "상품을 장바구니에 담는 API입니다.")
    public ResponseEntity<?> insertCart(@Valid @RequestBody CartMainDTO cart, BindingResult result
                                        , @AuthenticationPrincipal UserDetails userDetails
    ) {
        CartDTO cartItem;

        try {
            if (result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            cartItem = cartService.addCart(cart, email);

        } catch (CartException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(cartItem);
    }

    @PutMapping(value = "/item")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니물품 수정", description = "장바구니 물품 수량을 수정하는 API입니다.")
    public ResponseEntity<?> updateCart(@RequestBody CartUpdateDTO cartItem
                                    , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String res;

        try {

            String email = userDetails.getUsername();
            res = cartService.updateCart(cartItem, email);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping(value = "/items")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니삭제", description = "상품을 장바구니에서 삭제하는 API입니다.")
    public ResponseEntity<?> deleteCart(@RequestBody List<CartUpdateDTO> cartItems
                                        , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String res;

        try {

            String email = userDetails.getUsername();
            res = cartService.deleteCart(cartItems, email);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping(value = "/orderItems")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니구매예약", description = "장바구니 상품을 구매예약하는 API입니다.")
    public ResponseEntity<?> orderCart(@RequestBody List<CartOrderDTO> cartItems
                            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String res;

        try {

            String email = userDetails.getUsername();
            res = cartService.orderCart(cartItems, email);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping(value = "/cancelItems")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니구매예약취소", description = "장바구니 상품을 구매예약을 취소하는 API입니다.")
    public ResponseEntity<?> cancelOrderCart(@RequestBody List<CartOrderDTO> cartItems
                                            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String res;

        try {

            String email = userDetails.getUsername();
            res = cartService.cancelCartOrder(cartItems, email);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(res);
    }


    @GetMapping(value = "")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니내역조회", description = "장바구니 상품을 조회하는 API입니다.")
    public ResponseEntity<?> getCartList(
             @PageableDefault(sort = "cartItemId", direction = Sort.Direction.ASC) Pageable pageable
            ,@AuthenticationPrincipal UserDetails userDetails
    )    {

        Page<CartItemDTO> items = null;

        try {
            items = cartService.getCartPage(pageable, userDetails.getUsername());

            Map<String, Object> response = new HashMap<>();
            // 현재 페이지의 아이템 목록
            response.put("items", items.getContent());
            // 현재 페이지 번호
            response.put("nowPageNumber", items.getNumber());
            // 전체 페이지 수
            response.put("totalPage", items.getTotalPages());
            // 한 페이지에 출력되는 데이터 개수
            response.put("pageSize", items.getSize());
            // 다음 페이지 존재 여부
            response.put("hasNextPage", items.hasNext());
            // 이전 페이지 존재 여부
            response.put("hasPreviousPage", items.hasPrevious());
            // 첫 번째 페이지 여부
            response.put("isFirstPage", items.isFirst());
            // 마지막 페이지 여부
            response.put("isLastPage", items.isLast());

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
