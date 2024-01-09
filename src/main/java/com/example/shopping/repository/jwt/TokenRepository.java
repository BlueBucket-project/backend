package com.example.shopping.repository.jwt;

import com.example.shopping.entity.jwt.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/*
 *   writer : 유요한
 *   work :
 *          JWT 레포지토리
 *   date : 2023/10/04
 * */
@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    TokenEntity findByMemberEmail(String memberEmail);
}
