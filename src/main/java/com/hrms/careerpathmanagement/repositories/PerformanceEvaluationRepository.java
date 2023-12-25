package com.hrms.careerpathmanagement.repositories;

import com.hrms.performancemanagement.model.PerformanceEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PerformanceEvaluationRepository extends JpaRepository<PerformanceEvaluation, Integer> ,
        JpaSpecificationExecutor<PerformanceEvaluation>
{


    @Query("SELECT pe, e, p " +
            "FROM PerformanceEvaluation pe " +
            "INNER JOIN PerformanceCycle pc ON pe.performanceCycle.performanceCycleId = pc.performanceCycleId " +
            "INNER JOIN Employee e ON pe.employee.id = e.id " +
            "INNER JOIN Position p ON e.position.id = p.id " +
            "WHERE p.id = ?1 AND pc.performanceCycleId = ?2")

    List<PerformanceEvaluation> findByCycleIdAndPositionId(Integer positionId, Integer cycleId);

    <T> Collection<T> findAllByPerformanceCyclePerformanceCycleId(Integer cycleId, Class<T> type);

    @Query("SELECT avg(pe.finalAssessment) FROM PerformanceEvaluation pe " +
            "INNER JOIN PerformanceCycle pc ON pe.performanceCycle.performanceCycleId = pc.performanceCycleId " +
            "WHERE pe.id IN ?1")
    Double averageAssestmentScoreByIdIn(List<Integer> ids);

    default <T> Collection<T> findAllByCycleId(Integer cycleId, Class<T> type) {
        return findAllByPerformanceCyclePerformanceCycleId(cycleId, type);
    }
}