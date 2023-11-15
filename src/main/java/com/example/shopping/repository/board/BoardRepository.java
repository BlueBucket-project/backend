package com.example.shopping.repository.board;

import com.example.shopping.entity.board.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    @Query("select b from board b" +
            " join fetch b.member " +
            " where b.boardId = :boardId")
    Optional<BoardEntity> findByBoardId(@Param("boardId") Long boardId);

    void deleteByBoardId(Long boardId);

    @Query(value = "select b from board b" +
            " join fetch  b.member " +
            " where b.title like %:searchKeyword%" +
            " order by b.boardId DESC ",
            countQuery = "select count(b) from board b where b.title like %:searchKeyword%")
    Page<BoardEntity> findByTitleContaining(Pageable pageable,
                                            @Param("searchKeyword") String searchKeyword);

    @Query(value = "select b from board b" +
            " join fetch b.member " +
            " order by b.boardId DESC ",
            countQuery = "select count(b) from board b")
    Page<BoardEntity> findAll(Pageable pageable);

    @Query(value = "select b from board b " +
            "join fetch b.member " +
            "where b.member.email = :email" +
            " order by b.boardId DESC ",
            countQuery = "select count(b) from board b where b.member.email = :email"
    )
    Page<BoardEntity> findAllByMemberEmail(@Param("email") String email, Pageable pageable);

    @Query(value = "select  b from board  b " +
            " join fetch b.member " +
            " where b.member.email = :email and b.title like %:searchKeyword%" +
            " order by b.boardId DESC ",
            countQuery = "select count(b) from board b " +
                    "where b.member.email = :email and b.title like %:searchKeyword%")
    Page<BoardEntity> findByMemberEmailAndTitleContaining(@Param("email") String email,
                                                          Pageable pageable,
                                                          @Param("searchKeyword") String searchKeyword);

    @Query(value = "select b from board b " +
            " join fetch b.member " +
            "where b.member.nickName = :nickName" +
            " order by b.boardId DESC",
            countQuery = "select count(b) from board b where b.member.nickName = :nickName")
    Page<BoardEntity> findAllByMemberNickName(@Param("nickName") String nickName,
                                              Pageable pageable);

    @Query(value = "select b from board b " +
            " join fetch b.member " +
            "where b.member.nickName = :nickName and b.title like %:searchKeyword%" +
            " order by b.boardId desc ",
            countQuery = "select count(b) from board b " +
                    "where b.member.nickName = :nickName and b.title like %:searchKeyword%")
    Page<BoardEntity> findByMemberNickNameAndTitleContaining(@Param("nickName") String nickName,
                                                             Pageable pageable,
                                                             @Param("searchKeyword") String searchKeyword);

    @Query(value = "select b from board b " +
            " join fetch b.member " +
            " join fetch b.item " +
            "where b.item.itemId = :itemId" +
            " order by b.boardId desc ",
            countQuery = "select count(b) from board b where b.item.itemId = :itemId")
    Page<BoardEntity> findAllByItemItemId(@Param("itemId") Long itemId, Pageable pageable);

    @Query(value = "select b from board b " +
            " join fetch b.member " +
            " join fetch b.item " +
            "where b.item.itemId = :itemId and b.title like %:searchKeyword%" +
            " order by b.boardId desc ",
            countQuery = "select count(b) from board b " +
                    "where b.item.itemId = :itemId and b.title like %:searchKeyword%")
    Page<BoardEntity> findByItemItemIdContaining(@Param("itemId") Long itemId,
                                                 Pageable pageable,
                                                 @Param("searchKeyword") String searchKeyword);
}
