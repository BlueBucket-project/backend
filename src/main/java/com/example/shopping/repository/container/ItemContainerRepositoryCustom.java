package com.example.shopping.repository.container;

import com.example.shopping.entity.Container.ItemContainerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemContainerRepositoryCustom {
    Page<ItemContainerEntity> findAllPage(Pageable pageable);
}
