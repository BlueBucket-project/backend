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
    private Long id;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String memberEmail;
    private Date accessTokenTime;
    private Date refreshTokenTime;

    @Builder
    public TokenDTO(Long id, 
                    String grantType, 
                    String accessToken,
                    String refreshToken, 
                    String memberEmail, 
                    Date accessTokenTime, 
                    Date refreshTokenTime) {
        this.id = id;
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberEmail = memberEmail;
        this.accessTokenTime = accessTokenTime;
        this.refreshTokenTime = refreshTokenTime;
    }

    public static TokenDTO toTokenDTO(TokenEntity tokenEntity) {
        return TokenDTO.builder()
                .id(tokenEntity.getId())
                .grantType(tokenEntity.getGrantType())
                .accessToken(tokenEntity.getAccessToken())
                .accessTokenTime(tokenEntity.getAccessTokenTime())
                .refreshToken(tokenEntity.getRefreshToken())
                .refreshTokenTime(tokenEntity.getAccessTokenTime())
                .memberEmail(tokenEntity.getMemberEmail())
                .build();
    }
}
