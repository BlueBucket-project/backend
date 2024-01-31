package com.example.shopping.entity.Container;

import lombok.*;

import javax.persistence.*;
/*
 *   writer : 오현진
 *   work :
 *          상품을 등록할 때 상품이 어느 지점 창고에 있는지 등록하기
 *          위한 클래스
 *   date : 2023/12/06
 * */
@Embeddable
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContainerEntity{
    private String containerName;
    private String containerAddr;

    @Builder
    public ContainerEntity(String containerName,
                           String containerAddr) {
        this.containerName = containerName;
        this.containerAddr = containerAddr;
    }
}
