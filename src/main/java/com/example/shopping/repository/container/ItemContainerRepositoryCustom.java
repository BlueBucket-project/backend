package com.example.shopping.repository.container;

import com.example.shopping.entity.Container.ItemContainerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
/*
 *   writer : 유요한
 *   work :
 *          QueryDsl를 사용하기 위한 인터페이스
 *   date : 2024/01/26
 * */
public interface ItemContainerRepositoryCustom {
    Page<ItemContainerEntity> findAllPage(Pageable pageable);
}
