package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer>, JpaSpecificationExecutor<Promotion> {
}
