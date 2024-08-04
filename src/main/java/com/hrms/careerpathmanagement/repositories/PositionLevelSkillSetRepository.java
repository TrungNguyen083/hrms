package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.PositionLevelSkillSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PositionLevelSkillSetRepository extends JpaRepository<PositionLevelSkillSet, Integer>,
        JpaSpecificationExecutor<PositionLevelSkillSet>
{
}
