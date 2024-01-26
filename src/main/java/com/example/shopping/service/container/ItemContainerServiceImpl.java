package com.example.shopping.service.container;

import com.example.shopping.domain.container.ItemContainerDTO;
import com.example.shopping.repository.container.ItemContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 *   writer : 유요한
 *   work :
 *          상품 창고가 어느 위치에 있는지 관리하는 기능입니다.
 *   date : 2024/01/25
 * */
@Service
@RequiredArgsConstructor
public class ItemContainerServiceImpl implements ItemContainerService{
    private final ItemContainerRepository itemContainerRepository;

    // 상품의 판매지역을 보여줍니다.
    @Override
    @Transactional(readOnly = true)
    public Page<ItemContainerDTO> getSellPlaceList(Pageable pageable) {
        // 반환값이 컬렉션이기 때문에 .stream().map()을 사용합니다.
        return itemContainerRepository.findAllPage(pageable)
                .map(ItemContainerDTO::from);
    }
}
