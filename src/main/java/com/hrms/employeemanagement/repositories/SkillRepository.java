package com.hrms.employeemanagement.repositories;

import com.hrms.employeemanagement.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer>, JpaSpecificationExecutor<Skill> {
}
