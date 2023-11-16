package com.example.shopping.domain.jwt;

import com.example.shopping.entity.jwt.TokenEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String memberEmail;
    private Date accessTokenTime;
    private Date refreshTokenTime;
    private Long memberId;

    @Builder
    public TokenDTO(
                    String grantType, 
                    String accessToken,
                    String refreshToken, 
                    String memberEmail, 
                    Date accessTokenTime, 
                    Date refreshTokenTime,
                    Long memberId) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberEmail = memberEmail;
        this.accessTokenTime = accessTokenTime;
        this.refreshTokenTime = refreshTokenTime;
        this.memberId = memberId;
    }

    public static TokenDTO toTokenDTO(TokenEntity tokenEntity) {
        return TokenDTO.builder()
                .grantType(tokenEntity.getGrantType())
                .accessToken(tokenEntity.getAccessToken())
                .accessTokenTime(tokenEntity.getAccessTokenTime())
                .refreshToken(tokenEntity.getRefreshToken())
                .refreshTokenTime(tokenEntity.getAccessTokenTime())
                .memberEmail(tokenEntity.getMemberEmail())
                .memberId(tokenEntity.getMemberId())
                .build();
    }
}
