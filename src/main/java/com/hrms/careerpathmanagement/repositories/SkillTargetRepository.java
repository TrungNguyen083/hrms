package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.SkillTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SkillTargetRepository extends JpaRepository<SkillTarget, Integer>, JpaSpecificationExecutor<SkillTarget> {
}