package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.CompetencyEvaluationOverall;
import com.hrms.global.models.EvaluateCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CompetencyEvaluationOverallRepository extends JpaRepository<CompetencyEvaluationOverall, Integer>,
        JpaSpecificationExecutor<CompetencyEvaluationOverall>
{
    @Query("SELECT s.evaluateCycle FROM CompetencyEvaluationOverall s WHERE s.employee.id = ?1 AND s.finalStatus = 'Completed' ORDER BY s.evaluateCycle.startDate DESC LIMIT 1")
    EvaluateCycle latestEvalOByEvalCycle(Integer employeeId);
}
