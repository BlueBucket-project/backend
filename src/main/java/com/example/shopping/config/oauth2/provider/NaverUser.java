package com.example.shopping.config.oauth2.provider;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

/*
 *   writer : YuYoHan
 *   work :
 *          네이버에 대한 공통적이지 않은 정보를 빼오는 역할을 하고 있습니다.
 *   date : 2023/10/04
 * */
public class NaverUser extends OAuth2ProviderUser{
    public NaverUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super((Map<String, Object>) oAuth2User.getAttributes().get("response"),
                oAuth2User,
                clientRegistration);
    }

    @Override
    public String getProviderId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getName() {
        return (String) getAttributes().get("name");
    }
}
