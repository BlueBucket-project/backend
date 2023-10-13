package com.example.shopping.controller.cart;

import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.cart.CartMainDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(cartItem);
    }

    @PostMapping(value = "/item")
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

        } catch (CartException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
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

        } catch (CartException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(res);
    }
}
