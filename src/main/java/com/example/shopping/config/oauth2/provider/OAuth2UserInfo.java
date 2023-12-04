package com.example.shopping.config.oauth2.provider;

import java.util.Map;

// 정보를 가져올 것을 인터페이스로 뽑아놓고
// 상속받아서 사용하려고 합니다.
public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    Map<String, Object> getAttributes();
}
