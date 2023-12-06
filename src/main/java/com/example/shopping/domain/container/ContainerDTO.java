package com.example.shopping.domain.container;

import com.example.shopping.entity.Container.ContainerEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ContainerDTO {
    private Long containerId;

    private String containerName;

    private String containerAddr;

    @Builder
    public ContainerDTO(Long containerId, String containerName, String containerAddr) {
        this.containerId = containerId;
        this.containerName = containerName;
        this.containerAddr = containerAddr;
    }

    public static ContainerDTO from(ContainerEntity container){
        return ContainerDTO.builder()
                .containerId(container.getContainerId())
                .containerName(container.getContainerName())
                .containerAddr(container.getContainerAddr())
                .build();
    }
}
