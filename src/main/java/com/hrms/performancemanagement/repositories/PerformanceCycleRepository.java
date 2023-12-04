package com.hrms.performancemanagement.repositories;

import com.hrms.performancemanagement.model.PerformanceCycle;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PerformanceCycleRepository extends JpaRepository<PerformanceCycle, Integer>, JpaSpecificationExecutor<PerformanceCycle> {
    List<PerformanceCycle> findAll(Sort sort);

    Optional<Integer> findTopByOrderByPerformanceCycleIdDesc();

    PerformanceCycle findFirstByOrderByPerformanceCycleStartDateDesc();

    <T>Collection<T> findByPerformanceCycleId(Integer id, Class<T> type);
}
