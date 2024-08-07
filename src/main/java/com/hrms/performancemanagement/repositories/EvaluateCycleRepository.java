package com.hrms.performancemanagement.repositories;

import com.hrms.global.models.EvaluateCycle;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EvaluateCycleRepository extends JpaRepository<EvaluateCycle, Integer>, JpaSpecificationExecutor<EvaluateCycle> {
    List<EvaluateCycle> findAll(Sort sort);

    Optional<Integer> findTopByOrderByIdDesc();

    <T>Collection<T> findById(Integer id, Class<T> type);

    EvaluateCycle findFirstByOrderByStartDateDesc();

    EvaluateCycle findByYear(Integer year);
}
