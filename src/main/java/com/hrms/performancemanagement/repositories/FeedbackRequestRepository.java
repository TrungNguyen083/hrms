package com.hrms.performancemanagement.repositories;

import com.hrms.performancemanagement.dto.FeedbackDTO;
import com.hrms.global.models.FeedbackRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRequestRepository extends JpaRepository<FeedbackRequest, Integer>, JpaSpecificationExecutor<FeedbackRequest>
{

    @Query(value = "SELECT new com.hrms.performancemanagement.dto.FeedbackDTO(f.id, fr.feedbackReceiver.id, fr.feedbackReceiver.firstName, f.feedbackContent, f.createdAt)" +
            " FROM FeedbackRequest fr JOIN Feedback f ON fr.id = f.feedbackRequest.id " +
            "WHERE fr.feedbackReceiver.id = ?1 AND fr.evaluateCycle.id = ?2")
    public List<FeedbackDTO> findByFeedbackReceiverIdAndPerformanceCycleId(Integer feedbackReceiverId, Integer performanceCycleId);
}
