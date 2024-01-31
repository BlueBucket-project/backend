package com.example.shopping.service.container;

import com.example.shopping.domain.container.ItemContainerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemContainerService {
    // 상품 창고 검색
    Page<ItemContainerDTO> getSellPlaceList(Pageable pageable);
}
