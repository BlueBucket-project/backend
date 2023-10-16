package com.example.shopping.controller.cart;

import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartMainDTO;
import com.example.shopping.domain.cart.CartOrderDTO;
import com.example.shopping.domain.cart.CartUpdateDTO;
import com.example.shopping.exception.cart.CartException;
import com.example.shopping.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니담기", description = "상품을 장바구니에 담는 API입니다.")
    public ResponseEntity<?> insertCart(@RequestBody CartMainDTO cart, BindingResult result
                                        , @AuthenticationPrincipal UserDetails userDetails
    ) {
        CartItemDTO cartItem;

        try {
            if (result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            cartItem = cartService.addCart(cart, email);
            //test데이터 - 배포시 삭제필요
            //cartItem = cartService.addCart(cart, "test123@test.com");

        } catch (CartException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(cartItem);
    }

    @PutMapping(value = "/item")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니물품 수정", description = "장바구니 물품 수량을 수정하는 API입니다.")
    public ResponseEntity<?> updateCart(@RequestBody CartUpdateDTO cartItem, BindingResult result
                                    , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String res;

        try {
            if (result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            res = cartService.updateCart(cartItem, email);
            //test데이터 - 배포시 삭제필요
            //res = cartService.updateCart(cartItem, "test123@test.com");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping(value = "/items")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니삭제", description = "상품을 장바구니에서 삭제하는 API입니다.")
    public ResponseEntity<?> deleteCart(@RequestBody List<CartUpdateDTO> cartItems, BindingResult result
                                        , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String res;

        try {
            if (result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            res = cartService.deleteCart(cartItems, email);
            //test데이터 - 배포시 삭제필요
            //res = cartService.deleteCart(cartItems, "test123@test.com");


        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping(value = "/orderItems")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니구매예약", description = "장바구니 상품을 구매예약하는 API입니다.")
    public ResponseEntity<?> orderCart(@RequestBody List<CartOrderDTO> cartItems, BindingResult result
                            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String res;

        try {
            if (result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            res = cartService.orderCart(cartItems, email);
            //test데이터 - 배포시 삭제필요
            //res = cartService.orderCart(cartItems, "test123@test.com");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(res);
    }

    @PostMapping(value = "/cancelItems")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니구매예약취소", description = "장바구니 상품을 구매예약을 취소하는 API입니다.")
    public ResponseEntity<?> cancelOrderCart(@RequestBody List<CartOrderDTO> cartItems, BindingResult result
                                            , @AuthenticationPrincipal UserDetails userDetails
    ) {
        String res;

        try {
            if (result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            res = cartService.orderCart(cartItems, email);
            //test데이터 - 배포시 삭제필요
            //res = cartService.cancelCartOrder(cartItems, "test123@test.com");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body(res);
    }


    @GetMapping(value = "/{mbrEmail}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "장바구니내역조회", description = "장바구니 상품을 조회하는 API입니다.")
    public ResponseEntity<?> getCartList(@PathVariable String mbrEmail
                                        ,@AuthenticationPrincipal UserDetails userDetails
    )    {

        List<CartItemDTO> items = new ArrayList<>();

        try {
            items = cartService.getCartList(mbrEmail);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(items);
    }

}
