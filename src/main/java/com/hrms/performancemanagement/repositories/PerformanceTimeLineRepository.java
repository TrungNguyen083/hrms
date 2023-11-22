package com.hrms.performancemanagement.repositories;

import com.hrms.performancemanagement.model.PerformanceTimeLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceTimeLineRepository extends JpaRepository<PerformanceTimeLine, Integer>, JpaSpecificationExecutor<PerformanceTimeLine> {
}
