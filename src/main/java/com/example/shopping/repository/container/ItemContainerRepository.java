package com.example.shopping.repository.container;

import com.example.shopping.entity.Container.ContainerEntity;
import com.example.shopping.entity.Container.ItemContainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
/*
 *   writer : 유요한
 *   work :
 *          상품 창고 레포지토리
 *   date : 2024/01/07
 * */
public interface ItemContainerRepository extends JpaRepository<ItemContainerEntity, Long> {

    ContainerEntity findByContainerName(String sellPlace);
}
