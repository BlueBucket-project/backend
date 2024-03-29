package com.example.shopping.config.oauth2.provider;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

/*
 *   writer : YuYoHan
 *   work :
 *          Google에 대한 공통적이지 않은 정보를 빼오는 역할을 하고 있습니다.
 *   date : 2023/10/04
 * */

public class GoogleUser extends OAuth2ProviderUser{
    public GoogleUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        // 사용자의 정보는 oAuth2User.getAttributes() 여기에 담겨져 있다.
        // 여기는 클레임 형식 즉, Map 형식으로 되어 있다.
        super(oAuth2User.getAttributes(), oAuth2User, clientRegistration);
    }

    // 예) google
    @Override
    public String getProviderId() {
        return (String) getAttributes().get("sub");
    }


    @Override
    public String getName() {
        return (String) getAttributes().get("name");
    }
}
