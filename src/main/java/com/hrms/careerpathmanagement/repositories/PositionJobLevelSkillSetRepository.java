package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.PositionJobLevelSkillSet;
import com.hrms.careerpathmanagement.models.ProficiencyLevel;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface PositionJobLevelSkillSetRepository extends JpaRepository<PositionJobLevelSkillSet, Integer>,
        JpaSpecificationExecutor<PositionJobLevelSkillSet>
{

    @Query("SELECT avg(pl.score) FROM PositionJobLevelSkillSet pjls " +
            "JOIN ProficiencyLevel pl ON pjls.proficiencyLevel.id = pl.id " +
            "WHERE pjls.position.id = ?1 AND pjls.jobLevel.id = ?2")
    public Double averageSkillSetScoreByPositionLevel(Integer positionId, Integer levelId);
}
