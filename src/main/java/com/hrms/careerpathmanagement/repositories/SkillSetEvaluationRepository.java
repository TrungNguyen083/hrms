package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.SkillSetEvaluation;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.FluentQuery;

import java.util.function.Function;

public interface SkillSetEvaluationRepository extends JpaRepository<SkillSetEvaluation, Integer>, JpaSpecificationExecutor<SkillSetEvaluation> {
}
