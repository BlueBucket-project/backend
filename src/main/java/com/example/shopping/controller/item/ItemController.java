package com.example.shopping.controller.item;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {
    private final ItemService itemService;

    // 상품 등록
    @PostMapping("")
    public ResponseEntity<?> createItem(@Validated @RequestBody ItemDTO itemDTO,
                                        @RequestPart(value = "files")List<MultipartFile>itemFiles,
                                        BindingResult result,
                                        @AuthenticationPrincipal UserDetails userDetails){
        try {
            if(result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            ResponseEntity<?> responseEntity = itemService.saveItem(itemDTO, itemFiles, email);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
