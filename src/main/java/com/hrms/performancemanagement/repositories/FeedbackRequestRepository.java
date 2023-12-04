package com.hrms.performancemanagement.repositories;

import com.hrms.performancemanagement.model.FeedbackRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FeedbackRequestRepository extends JpaRepository<FeedbackRequest, Integer>, JpaSpecificationExecutor<FeedbackRequest>
{
}
