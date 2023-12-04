package com.hrms.spring;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

public class CustomRepository<T, ID> extends SimpleJpaRepository<T, ID> implements QueryByExampleExecutor<T>
{
    private final EntityManager entityManager;

    public CustomRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    private String getAverageQueryString(String columnName, Iterable<ID> ids) {
        String query = String.format("SELECT AVG(%s) FROM %s WHERE id IN :ids", columnName, getDomainClass().getName());
        return query;
    }


    public Double averageByIdIn(String columnName, Iterable<ID> ids) {
        if (ids == null || !ids.iterator().hasNext()) {
            throw new IllegalArgumentException("IDs cannot be null or empty");
        }

        String queryString = String.format("SELECT AVG(%s) FROM %s WHERE id IN :ids", columnName, getDomainClass().getName());
        TypedQuery<Double> query = entityManager.createQuery(queryString, Double.class);
        query.setParameter("ids", ids);

        List<Double> results = query.getResultList();
        return  (results.isEmpty() || results.get(0) == null) ? 0 : results.get(0);
    }

    public Double averageBy(String columnsName, Predicate predicate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<T> root = cq.from(getDomainClass());
        cq.select(cb.avg(root.get(columnsName))).where(predicate);

        return entityManager.createQuery(cq).getSingleResult();
    }
}