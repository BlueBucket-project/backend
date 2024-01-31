package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartStatus;
import com.example.shopping.entity.cart.CartItemEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/*
 *   writer : 오현진, 유요한
 *   work :
 *          장바구니 상품 레포지토리
 *          Spring Data JPA 방식을 사용하였고 fetch Join을 위하여
 *          JPQL을 사용했습니다.
 *   date : 2024/01/29
 * */
public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {

    CartItemEntity findByCartCartIdAndItemItemIdAndStatusNot(Long cartId,
                                                             Long itemId,
                                                             CartStatus status);
    CartItemEntity findByCartCartIdAndItemItemId(Long cartId, Long itemId);

    List<CartItemEntity> findByCartCartId(Long cartId);


    @Query("SELECT c FROM cartitem c JOIN FETCH c.cart WHERE c.cart.cartId = :cartId " +
            "AND c.status <> :status")
    List<CartItemEntity> findByCartCartIdAndStatusNot(@Param("cartId") Long cartId,
                                                      @Param("status")CartStatus status);

    @Query("SELECT c FROM cartitem c JOIN FETCH c.cart WHERE c.cart.cartId = :cartId " +
            "AND c.status = :status")
    List<CartItemEntity> findByCartCartIdAndStatus(@Param("cartId") Long cartId,
                                                   @Param("status")CartStatus status);

    List<CartItemEntity> findByItemItemId(Long itemId);

    // 벌크 연산
    // 데이터베이스의 부하를 줄이기 위해서 사용
    @Modifying(clearAutomatically = true)
    @Transactional
    // fetch join과 같습니다.
    // 상품 엔티티를 한번에 가져오기 위해서 사용하고 있습니다.
    @EntityGraph(attributePaths = "item")
    @Query("update cartitem c set c.status = :newStatus where c.cartitemId in :cartItemIds")
    List<CartItemEntity> updateCartItemsStatus(@Param("newStatus") CartStatus newStatus,
                                               @Param("cartItemIds") List<Long> cartItemIds);
}
