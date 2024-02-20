package com.example.shopping.repository.board;

import com.example.shopping.entity.board.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/*
 *   writer : 유요한
 *   work :
 *          게시글 레포지토리
 *          Spring Data JPA 방식을 사용하였고 fetch Join을 위하여
 *          JPQL을 사용했습니다.
 *   date : 2024/01/09
 * */
@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    @Query("select b from board b" +
            " join fetch b.member " +
            " join fetch b.item " +
            " where b.boardId = :boardId")
    Optional<BoardEntity> findByBoardId(@Param("boardId") Long boardId);

    void deleteByBoardId(Long boardId);
    void deleteAllByMemberMemberId(Long memberId);

    @Query(value = "select distinct b from board b" +
            " join fetch  b.member " +
            " join fetch b.item " +
            " where (:searchKeyword is null or b.title like %:searchKeyword%)" +
            " order by b.boardId DESC ",
            countQuery = "select count(b) from board b where (:searchKeyword is null or b.title like %:searchKeyword%)")
    Page<BoardEntity> findByTitleContaining(Pageable pageable,
                                            @Param("searchKeyword") String searchKeyword);

    @Query(value = "select distinct b from board b" +
            " join fetch b.member " +
            " join fetch b.item " +
            " order by b.boardId DESC ",
            countQuery = "select count(b) from board b")
    Page<BoardEntity> findAll(Pageable pageable);


    @Query(value = "select distinct b from board  b " +
            " join fetch b.member " +
            " join fetch b.item " +
            " where b.member.email = :email and (:searchKeyword is null or b.title like %:searchKeyword%)" +
            " order by b.boardId DESC ",
            countQuery = "select count(b) from board b " +
                    "where b.member.email = :email and (:searchKeyword is null or b.title like %:searchKeyword%)")
    Page<BoardEntity> findByMemberEmailAndTitleContaining(@Param("email") String email,
                                                          Pageable pageable,
                                                          @Param("searchKeyword") String searchKeyword);


    @Query(value = "select distinct b from board b " +
            " join fetch b.member " +
            " join fetch b.item " +
            "where b.member.nickName = :nickName and (:searchKeyword is null or b.title like %:searchKeyword%)" +
            " order by b.boardId desc ",
            countQuery = "select count(b) from board b " +
                    "where b.member.nickName = :nickName and (:searchKeyword is null or b.title like %:searchKeyword%)")
    Page<BoardEntity> findByMemberNickNameAndTitleContaining(@Param("nickName") String nickName,
                                                             Pageable pageable,
                                                             @Param("searchKeyword") String searchKeyword);

    @Query(value = "select distinct b from board b " +
            " join fetch b.member " +
            " join fetch b.item " +
            "where b.item.itemId = :itemId" +
            " order by b.boardId desc ",
            countQuery = "select count(b) from board b where b.item.itemId = :itemId")
    Page<BoardEntity> findAllByItemItemId(@Param("itemId") Long itemId, Pageable pageable);

}
