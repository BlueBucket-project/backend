package com.example.shopping.repository.container;

import com.example.shopping.entity.Container.ContainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainerRepository extends JpaRepository<ContainerEntity, Long> {


    ContainerEntity findByContainerName(String sellPlace);
}
