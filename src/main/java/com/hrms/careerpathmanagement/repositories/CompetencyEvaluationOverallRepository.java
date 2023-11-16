package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.CompetencyEvaluationOverall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompetencyEvaluationOverallRepository extends JpaRepository<CompetencyEvaluationOverall, Integer>,
        JpaSpecificationExecutor<CompetencyEvaluationOverall>
{
}
