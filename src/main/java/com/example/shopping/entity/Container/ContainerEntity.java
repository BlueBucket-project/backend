package com.example.shopping.entity.Container;

import com.example.shopping.entity.Base.BaseEntity;
import com.example.shopping.entity.Base.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "container")
@Table
@Getter
@NoArgsConstructor
public class ContainerEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long containerId;

    String containerName;

    String containerAddr;
}
