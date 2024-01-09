package com.example.shopping.repository.item.support;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;
/*
 *   writer : YuYoHan
 *   work :
 *          스프링 데이터가 제공하는 QuerydslRepositorySupport가 지닌 한계를 극복하기 위해 직접 Querydsl 지원 클래스
 *   date : 2024/01/05
 * */
@Repository
public abstract class Querydsl4RepositorySupport {
    // 이 클래스가 다루는 도메인(엔티티)의 클래스
    private final Class domainClass;
    // 도메인 엔티티에 대한 Querydsl 쿼리를 생성하고 실행
    private Querydsl querydsl;
    // 데이터베이스와의 상호 작용을 담당하는 JPA의 핵심 객체
    private EntityManager entityManager;
    // queryFactory를 통해 Querydsl 쿼리를 생성하고 실행
    private JPAQueryFactory queryFactory;

    public Querydsl4RepositorySupport(Class<?> domainClass) {
        Assert.notNull(domainClass, "도메인 클래스는 null이면 안됩니다.");
        this.domainClass = domainClass;
    }

    // Pageable 안에 있는 Sort를 사용할 수 있도록 설정
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "엔티티메니저는 null이면 안됩니다.");
        // JpaEntityInformation을 얻기 위해 JpaEntityInformationSupport를 사용합니다.
        // 이 정보는 JPA 엔터티에 대한 메타데이터 및 정보를 제공합니다.
        JpaEntityInformation entityInformation =
                JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);

        // Querydsl에서 엔터티의 경로를 생성하는 데 사용됩니다.
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        // entityInformation을 기반으로 엔티티의 경로를 생성합니다.
        EntityPath path = resolver.createPath(entityInformation.getJavaType());
        this.entityManager = entityManager;
        // querydsl 객체를 생성합니다.
        // 이 객체는 Querydsl의 핵심 기능을 사용할 수 있도록 도와줍니다.
        // 엔터티의 메타모델 정보를 이용하여 Querydsl의 PathBuilder를 생성하고, 이를 이용하여 Querydsl 객체를 초기화합니다.
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    // 해당 클래스의 빈(Bean)이 초기화될 때 자동으로 실행되는 메서드
    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
    }
    // 이 팩토리는 JPA 쿼리를 생성하는 데 사용됩니다.
    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }
    // 이 객체는 Querydsl의 핵심 기능을 사용하는 데 도움이 됩니다.
    protected Querydsl getQuerydsl() {
        return querydsl;
    }
    // EntityManager는 JPA 엔터티를 관리하고 JPA 쿼리를 실행하는 데 사용됩니다.
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    // Querydsl을 사용하여 쿼리의 SELECT 절을 생성하는 메서드입니다.
    // expr은 선택할 엔터티나 엔터티의 속성에 대한 표현식입니다.
    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return getQueryFactory().select(expr);
    }
    // Querydsl을 사용하여 쿼리의 FROM 절을 생성하는 메서드입니다.
    // from은 엔터티에 대한 경로 표현식입니다.
    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return getQueryFactory().selectFrom(from);
    }

    // 이 메서드는 주어진 contentQuery를 사용하여 Querydsl을 통해 JPA 쿼리를 생성하고 실행하고,
    // 그 결과를 Spring Data의 Page 객체로 변환하는 기능을 제공
    protected <T> Page<T> applyPagination(Pageable pageable,
                                          Function<JPAQueryFactory, JPAQuery> contentQuery) {
        // 1. contentQuery를 사용하여 JPAQuery 객체를 생성
        JPAQuery jpaQuery = contentQuery.apply(getQueryFactory());
        // 2. Querydsl을 사용하여 페이징 및 정렬된 결과를 가져옴
        List<T> content = getQuerydsl().applyPagination(pageable,
                jpaQuery).fetch();
        // 3. contentQuery를 다시 사용하여 countQuery를 생성
        JPAQuery<Long> countQuery = contentQuery.apply(getQueryFactory());
        // 4. countQuery를 실행하고 총 레코드 수를 얻음
        long total = countQuery.fetchOne();
        // 5. content와 pageable 정보를 사용하여 Spring Data의 Page 객체를 생성하고 반환
        return PageableExecutionUtils.getPage(content, pageable,
                () -> total);
    }
    // 이 메서드는 contentQuery와 함께 countQuery를 인자로 받아서 사용합니다.
    // contentQuery를 사용하여 페이징된 결과를 가져오고, countQuery를 사용하여 전체 레코드 수를 얻습니다.

    protected <T> Page<T> applyPagination(Pageable pageable,
                                          Function<JPAQueryFactory, JPAQuery> contentQuery, Function<JPAQueryFactory,
            JPAQuery> countQuery) {
        JPAQuery jpaContentQuery = contentQuery.apply(getQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable,
                jpaContentQuery).fetch();

        JPAQuery<Long> countResult = countQuery.apply(getQueryFactory());
        Long total = countResult.fetchOne();
        return PageableExecutionUtils.getPage(content, pageable,
                () -> total);
    }

}
