package com.hrms.careerpathmanagement.repositories;

import com.hrms.global.models.PositionLevelSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PositionLevelSkillRepository extends JpaRepository<PositionLevelSkill, Integer>,
        JpaSpecificationExecutor<PositionLevelSkill>
{
}
