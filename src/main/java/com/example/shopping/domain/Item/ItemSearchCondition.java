package com.example.shopping.domain.Item;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
/*
 *   writer : 유요한
 *   work :
 *          상품을 조건을 넣고 검색할 때 동적으로 처리하기 위해서
 *          조건을 받는데 그 때 객체에 파라미터를 받아서 처리하기 위한 곳입니다.
 *   date : 2024/01/05
 * */
@ToString
@Getter
public class ItemSearchCondition {
    private String name;
    private String detail;
    // 시작 가격
    private Long startP;
    // 끝 가격
    private Long endP;
    // 장소
    private String place;
    // 예약자
    private String reserver;
    // 상품 상태
    private ItemSellStatus status;

    @Builder
    public ItemSearchCondition(String name,
                               String detail,
                               Long startP,
                               Long endP,
                               String place,
                               String reserver,
                               ItemSellStatus status) {
        this.name = name;
        this.detail = detail;
        this.startP = startP;
        this.endP = endP;
        this.place = place;
        this.reserver = reserver;
        this.status = status;
    }
}
