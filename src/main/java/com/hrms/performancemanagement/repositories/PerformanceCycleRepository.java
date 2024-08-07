package com.hrms.performancemanagement.repositories;

import com.hrms.global.models.PerformanceCycle;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PerformanceCycleRepository extends JpaRepository<PerformanceCycle, Integer>, JpaSpecificationExecutor<PerformanceCycle> {
    List<PerformanceCycle> findAll(Sort sort);

    Optional<Integer> findTopByOrderByIdDesc();

    PerformanceCycle findFirstByOrderByPerformanceCycleStartDateDesc();

    <T>Collection<T> findById(Integer id, Class<T> type);
}
