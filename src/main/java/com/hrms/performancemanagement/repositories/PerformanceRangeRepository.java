package com.hrms.performancemanagement.repositories;

import com.hrms.performancemanagement.model.PerformanceRange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRangeRepository extends JpaRepository<PerformanceRange, Integer> {
}
