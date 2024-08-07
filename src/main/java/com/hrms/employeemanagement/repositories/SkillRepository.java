package com.hrms.employeemanagement.repositories;

import com.hrms.global.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SkillRepository extends JpaRepository<Skill, Integer>, JpaSpecificationExecutor<Skill> {
}
