package com.example.shopping.repository.board;

import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long>{
    void deleteByBoardId(Long boardId);
    Page<BoardEntity> findByTitleContaining(Pageable pageable, String searchKeyword);
    Page<BoardEntity> findAll(Pageable pageable);
    Page<BoardEntity> findAllByMemberEmail(String email, Pageable pageable);
    Page<BoardEntity> findByMemberEmailAndTitleContaining(String email, Pageable pageable, String searchKeyword);

    Page<BoardEntity> findAllByMemberNickName(String nickName, Pageable pageable);
    Page<BoardEntity> findByMemberNickNameAndTitleContaining(String nickName, Pageable pageable, String searchKeyword);
    Page<BoardEntity> findAllByItemItemId(Long itemId, Pageable pageable);
    Page<BoardEntity> findByItemItemIdContaining(Long itemId, Pageable pageable, String searchKeyword);
}
