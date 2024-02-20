package com.example.shopping.repository.container;

import com.example.shopping.entity.Container.ItemContainerEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.shopping.entity.Container.QItemContainerEntity.*;
import static com.example.shopping.entity.item.QItemEntity.itemEntity;
import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
/*
 *   writer : 유요한
 *   work :
 *          QueryDsl를 사용하기 위해서 구현한 클래스
 *          정렬은 동적으로 처리하고 있습니다.
 *   date : 2024/01/26
 * */
@RequiredArgsConstructor
public class ItemContainerRepositoryImpl implements ItemContainerRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ItemContainerEntity> findAllPage(Pageable pageable) {
        JPAQuery<ItemContainerEntity> query = queryFactory
                .selectFrom(itemContainerEntity)
                .join(itemContainerEntity.item, itemEntity).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Order order : pageable.getSort()) {
            // PathBuilder는 주어진 엔터티의 동적인 경로를 생성하는 데 사용됩니다.
            PathBuilder pathBuilder = new PathBuilder(
                    // 엔티티의 타입 정보를 얻어온다.
                    itemContainerEntity.getType(),
                    // 엔티티의 메타데이터를 얻어온다.
                    itemContainerEntity.getMetadata()
            );
            // Order 객체에서 정의된 속성에 해당하는 동적 경로를 얻어오게 됩니다.
            // 예를 들어, 만약 order.getProperty()가 "userName"이라면,
            // pathBuilder.get("userName")는 엔터티의 "userName" 속성에 대한 동적 경로를 반환하게 됩니다.
            // 이 동적 경로는 QueryDSL에서 사용되어 정렬 조건을 만들 때 활용됩니다.
            PathBuilder sort = pathBuilder.get(order.getProperty());

            query.orderBy(
                    new OrderSpecifier<>(
                            order.isDescending() ? DESC : ASC,
                            sort != null ? sort : itemContainerEntity.id
                    ));
        }

        List<ItemContainerEntity> result = query.fetch();
        JPAQuery<Long> count = queryFactory
                .select(itemContainerEntity.count())
                .from(itemContainerEntity);
        return PageableExecutionUtils.getPage(result, pageable, count::fetchOne);
    }
}
