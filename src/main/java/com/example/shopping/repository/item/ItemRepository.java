package com.example.shopping.repository.item;

import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.entity.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    ItemEntity deleteByItemId(Long itemId);

    // 페이지 처리를 위해서
    Page<ItemEntity> findAll(Pageable pageable);
    // 검색
    Page<ItemEntity> findByItemNameContaining(Pageable pageable, String searchKeyword);

    ItemEntity findByItemId(Long itemId);

    //JPA Method로 사용할 수 없는 경우 nativeQuery를 사용해야하며 이는 sql문법대로 써야함
    @Query(value="select * from item i where (:name is null or i.item_name like :name)" +
            "and (:detail is null or i.item_detail like :detail)" +
            "and (:startP is null or i.item_price between :startP and :endP)" +
            "and (:place is null or i.item_place like :place)" +
            "and (:reserver is null or i.item_reserver = :reserver)" +
            "and (:status is null or i.item_sell_status like :status) order by reg_time DESC", nativeQuery = true)
    List<ItemEntity> findByConditions(@Param("name") String name,
                                      @Param("detail")String detail,
                                      @Param("startP")Long startP,
                                      @Param("endP")Long endP,
                                      @Param("place")String place,
                                      @Param("reserver")String reserver,
                                      @Param("status")String status);
}
