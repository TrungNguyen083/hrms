package com.hrms.performancemanagement.services;

import com.hrms.careerpathmanagement.dto.TemplateDTO;
import com.hrms.global.models.EvaluateCycle;
import com.hrms.global.models.Template;
import com.hrms.careerpathmanagement.repositories.*;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.performancemanagement.dto.CategoryDTO;
import com.hrms.performancemanagement.dto.FeedbackDTO;
import com.hrms.performancemanagement.dto.PerformanceEvalTemplateDTO;
import com.hrms.global.models.FeedbackRequest;
import com.hrms.performancemanagement.projection.TemplateIdOnly;
import com.hrms.performancemanagement.repositories.FeedbackRequestRepository;
import com.hrms.performancemanagement.repositories.EvaluateCycleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PerformanceTemplateService {
    @PersistenceContext
    EntityManager em;
    private final CategoryQuestionRepository categoryQuestionRepository;
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;
    private final TemplateCategoryRepository templateCategoryRepository;
    private final PerformanceEvaluationRepository performanceEvaluationRepository;
    private final EvaluateCycleRepository evaluateCycleRepository;
    private final FeedbackRequestRepository feedbackRequestRepository;
    @Autowired
    public PerformanceTemplateService(CategoryQuestionRepository categoryQuestionRepository,
                                      QuestionRepository questionRepository,
                                      CategoryRepository categoryRepository,
                                      TemplateRepository templateRepository,
                                      TemplateCategoryRepository templateCategoryRepository, PerformanceEvaluationRepository performanceEvaluationRepository, EvaluateCycleRepository evaluateCycleRepository, FeedbackRequestRepository feedbackRequestRepository)
    {
        this.categoryQuestionRepository = categoryQuestionRepository;
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.templateRepository = templateRepository;
        this.templateCategoryRepository = templateCategoryRepository;
        this.performanceEvaluationRepository = performanceEvaluationRepository;
        this.evaluateCycleRepository = evaluateCycleRepository;
        this.feedbackRequestRepository = feedbackRequestRepository;
    }

    public Template getTemplateOfCycle(Integer cycleId) {
        var templateId = evaluateCycleRepository
                .findById(cycleId, TemplateIdOnly.class)
                .stream()
                .map(TemplateIdOnly::templateId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Template not found"));

        return templateRepository.findById(templateId).orElseThrow(() -> new RuntimeException("Template not found"));
    }

    public PerformanceEvalTemplateDTO getTemplateAndQuestion(Integer cycleId) {

        var template = getTemplateOfCycle(cycleId);
        var categories = templateCategoryRepository.findAllByTemplateId(template.getId());
        var categoryIds = categories.stream().map(i -> i.getCategory().getId()).toList();

        var categoryQuestions = categoryQuestionRepository.findAllByCategoryIdIn(categoryIds);

        var templateDTO = TemplateDTO.builder()
                .id(template.getId())
                .templateName(template.getTemplateName())
                .templateDescription(template.getTemplateDescription())
                .createdAt(template.getCreatedAt().toString())
                .createdById(template.getCreatedBy().getId())
                .build();

        var evalTemplate = new PerformanceEvalTemplateDTO(templateDTO);

        var categoriesDTO = categories.stream().map(item ->
        {
            return CategoryDTO.builder()
                    .categoryId(item.getCategory().getId())
                    .categoryName(item.getCategory().getCategoryName())
                    .questions(new ArrayList<>())
                    .build();
        }).toList();

        categoryQuestions.forEach(q ->
            categoriesDTO.stream()
                    .filter(c -> c.getCategoryId().equals(q.getCategory().getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Category not found"))
                    .addQuestion(q.getQuestion())
        );

        evalTemplate.setCategories(categoriesDTO);

        return evalTemplate;
    }

    public String createFeedbackRequest(Integer requestorId,
                                      List<Integer> requestReceiverIds,
                                      Integer cycleId,
                                      Integer feedbackReceiverId,
                                      String message)
    {
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
