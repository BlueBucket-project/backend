package com.example.shopping.repository.item;

import com.example.shopping.entity.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/*
 *   writer : 유요한
 *   work :
 *          상품 레포지토리
 *   date : 2024/01/09
 * */
@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    void deleteByItemId(Long itemId);

    // 페이지 처리를 위해서
    Page<ItemEntity> findAll(Pageable pageable);

    ItemEntity findByItemId(Long itemId);

}
