package com.hrms.performancemanagement.services;

import com.hrms.global.models.EvaluateCycle;
import com.hrms.careerpathmanagement.repositories.*;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.performancemanagement.dto.FeedbackDTO;
import com.hrms.global.models.FeedbackRequest;
import com.hrms.performancemanagement.repositories.FeedbackRequestRepository;
import com.hrms.performancemanagement.repositories.EvaluateCycleRepository;
import com.hrms.performancemanagement.repositories.PerformanceEvaluationOverallRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PerformanceTemplateService {
    @PersistenceContext
    EntityManager em;
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final PerformanceEvaluationOverallRepository performanceEvaluationOverallRepository;
    private final EvaluateCycleRepository evaluateCycleRepository;
    private final FeedbackRequestRepository feedbackRequestRepository;

    @Autowired
    public PerformanceTemplateService(
            QuestionRepository questionRepository,
            CategoryRepository categoryRepository,
            PerformanceEvaluationOverallRepository performanceEvaluationOverallRepository,
            EvaluateCycleRepository evaluateCycleRepository,
            FeedbackRequestRepository feedbackRequestRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.performanceEvaluationOverallRepository = performanceEvaluationOverallRepository;
        this.evaluateCycleRepository = evaluateCycleRepository;
        this.feedbackRequestRepository = feedbackRequestRepository;
    }

    public String createFeedbackRequest(Integer requestorId,
                                        List<Integer> requestReceiverIds,
                                        Integer cycleId,
                                        Integer feedbackReceiverId,
                                        String message) {
        var requestor = new Employee();
        requestor.setId(requestorId);

        var feedbackReceiver = new Employee();
        feedbackReceiver.setId(feedbackReceiverId);

        var cycle = new EvaluateCycle();
        cycle.setId(cycleId);

        requestReceiverIds.forEach(id -> {
            var requestReceiver = new Employee();
            requestReceiver.setId(id);

            feedbackRequestRepository.save(new FeedbackRequest(
                    null,
                    requestor, requestReceiver, feedbackReceiver,
                    cycle, message, new Date()
            ));
        });

        return "Feedback request created";
    }

    public List<FeedbackDTO> getFeedbacks(Integer feedbackReceiverId, Integer cycleId) {
        var feedbackRequests = feedbackRequestRepository
                .findByFeedbackReceiverIdAndPerformanceCycleId(feedbackReceiverId, cycleId);
        feedbackRequests.forEach(System.out::println);
        return null;
    }
}
