package com.example.shopping.repository.comment;

import com.example.shopping.entity.comment.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/*
 *   writer : 유요한
 *   work :
 *          댓글 레포지토리
 *   date : 2023/12/07
 * */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    void deleteAllByMemberMemberId(Long memberId);
}
