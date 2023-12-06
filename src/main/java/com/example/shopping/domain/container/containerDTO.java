package com.example.shopping.domain.container;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class containerDTO {
    private Long containerId;

    private String containerName;

    private String containerAddr;
}
