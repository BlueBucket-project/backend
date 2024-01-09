package com.example.shopping.repository.member;

import com.example.shopping.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/*
 *   writer : 유요한
 *   work :
 *          유저 레포지토리
 *   date : 2023/10/11
 * */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByEmail(String email);
    void deleteByMemberId(Long memberId);
    MemberEntity findByNickName(String nickName);
}
