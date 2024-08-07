package com.hrms.careerpathmanagement.repositories;

import com.hrms.global.models.CompetencyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompetencyGroupRepository extends JpaRepository<CompetencyGroup, Integer>, JpaSpecificationExecutor<CompetencyGroup> {
}
