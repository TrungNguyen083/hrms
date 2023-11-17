package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.PositionJobLevelSkillSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionJobLevelSkillSetRepository extends JpaRepository<PositionJobLevelSkillSet, Integer>,
        JpaSpecificationExecutor<PositionJobLevelSkillSet>
{
}
