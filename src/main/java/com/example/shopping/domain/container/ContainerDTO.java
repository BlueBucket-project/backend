package com.example.shopping.domain.container;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 *   writer : 오현진
 *   work :
 *          판매 지역과 지점에 대한 정보를 보내주는 RequestDTO
 *   date : 2023/01/07
 * */
@Getter
@NoArgsConstructor
@ToString
public class ContainerDTO {
    private String containerName;
    private String containerAddr;

    @Builder
    public ContainerDTO(String containerName, String containerAddr) {
        this.containerName = containerName;
        this.containerAddr = containerAddr;
    }


}
