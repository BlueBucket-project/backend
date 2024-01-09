package com.example.shopping.config.oauth2.provider;

import java.util.Map;

/*
 *   writer : YuYoHan
 *   work :
 *          정보를 가져올 것을 인터페이스로 뽑아놓고
 *          상속을 받아서 사용합니다.
 *   date : 2023/10/04
 * */
public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    Map<String, Object> getAttributes();
}
