package com.hrms.performancemanagement.controllers;

import com.hrms.performancemanagement.dto.FeedbackDTO;
import com.hrms.performancemanagement.dto.PerformanceEvalTemplateDTO;
import com.hrms.performancemanagement.services.PerformanceTemplateService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EvaluationTemplateController {
    private final PerformanceTemplateService performanceTemplateService;

    public EvaluationTemplateController(PerformanceTemplateService performanceTemplateService) {
        this.performanceTemplateService = performanceTemplateService;
    }

    @QueryMapping(name = "templatedAndQuestion")
    @PreAuthorize("hasAnyAuthority('USER') or hasAuthority('MANAGER')")
    public PerformanceEvalTemplateDTO getTemplateAndQuestion(@Argument Integer cycleId) {
        return performanceTemplateService.getTemplateAndQuestion(cycleId);
    }

    @MutationMapping(name = "createFeedbackRequest")
    @PreAuthorize("hasAuthority('MANAGER')")
    public String requestFeedback(@Argument Integer requestorId,
                                @Argument List<Integer> requestReceiverIds,
                                @Argument Integer cycleId,
                                @Argument Integer feedbackReceiverId,
                                @Argument String message)
    {
        return performanceTemplateService.createFeedbackRequest(requestorId,
                requestReceiverIds,
                cycleId,
                feedbackReceiverId,
                message
        );
    }

    @QueryMapping(name = "feedbacks")
    @PreAuthorize("hasAnyAuthority('USER') or hasAuthority('MANAGER')")
    public List<FeedbackDTO> getFeedbacks(@Argument Integer feedbackReceiverId,
                                          @Argument Integer cycleId) {
        return performanceTemplateService.getFeedbacks(feedbackReceiverId, cycleId);
    }
}
