package com.hrms.performancemanagement.controllers;

import com.hrms.performancemanagement.dto.PerformanceEvalTemplateDTO;
import com.hrms.performancemanagement.services.PerformanceTemplateService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EvaluationTemplateController {
    private final PerformanceTemplateService performanceTemplateService;

    public EvaluationTemplateController(PerformanceTemplateService performanceTemplateService) {
        this.performanceTemplateService = performanceTemplateService;
    }

    @QueryMapping(name = "templatedAndQuestion")
    public PerformanceEvalTemplateDTO getTemplateAndQuestion(@Argument Integer cycleId) {
        return performanceTemplateService.getTemplateAndQuestion(cycleId);
    }

    public void requestFeedback(@Argument Integer requestorId,
                                @Argument Integer requestReceiverId,
                                @Argument Integer cycleId,
                                @Argument List<Integer> feedbackReceiverIds)
    {
        performanceTemplateService.createFeedbackRequest(requestorId, requestReceiverId, cycleId, feedbackReceiverIds);
    }
}
