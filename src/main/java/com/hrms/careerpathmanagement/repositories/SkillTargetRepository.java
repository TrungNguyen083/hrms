package com.hrms.careerpathmanagement.repositories;

import com.hrms.global.models.SkillTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SkillTargetRepository extends JpaRepository<SkillTarget, Integer>, JpaSpecificationExecutor<SkillTarget> {
}
