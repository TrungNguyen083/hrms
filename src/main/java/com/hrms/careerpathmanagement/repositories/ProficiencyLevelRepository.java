package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.ProficiencyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProficiencyLevelRepository extends JpaRepository<ProficiencyLevel, Integer>, JpaSpecificationExecutor<ProficiencyLevel> {
    ProficiencyLevel findFirstByOrderByScoreDesc();
    //Optional<Float> ();
}
