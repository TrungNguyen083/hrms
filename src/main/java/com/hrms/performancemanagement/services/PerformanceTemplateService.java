package com.hrms.performancemanagement.services;

import com.hrms.careerpathmanagement.models.CategoryQuestion;
import com.hrms.careerpathmanagement.models.Question;
import com.hrms.careerpathmanagement.models.Template;
import com.hrms.careerpathmanagement.repositories.*;
import com.hrms.performancemanagement.dto.CategoryDTO;
import com.hrms.performancemanagement.dto.TemplateDTO;
import com.hrms.performancemanagement.dto.PerformanceEvalTemplateDTO;
import com.hrms.performancemanagement.model.PerformanceCycle;
import com.hrms.performancemanagement.projection.TemplateIdOnly;
import com.hrms.performancemanagement.repositories.PerformanceCycleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerformanceTemplateService {
    @PersistenceContext
    EntityManager em;
    private final CategoryQuestionRepository categoryQuestionRepository;
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;
    private final TemplateCategoryRepository templateCategoryRepository;
    private final PerformanceEvaluationRepository performanceEvaluationRepository;
    private final PerformanceCycleRepository performanceCycleRepository;
    @Autowired
    public PerformanceTemplateService(CategoryQuestionRepository categoryQuestionRepository,
                                      QuestionRepository questionRepository,
                                      CategoryRepository categoryRepository,
                                      TemplateRepository templateRepository,
                                      TemplateCategoryRepository templateCategoryRepository, PerformanceEvaluationRepository performanceEvaluationRepository, PerformanceCycleRepository performanceCycleRepository)
    {
        this.categoryQuestionRepository = categoryQuestionRepository;
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.templateRepository = templateRepository;
        this.templateCategoryRepository = templateCategoryRepository;
        this.performanceEvaluationRepository = performanceEvaluationRepository;
        this.performanceCycleRepository = performanceCycleRepository;
    }

    public Template getTemplateOfCycle(Integer cycleId) {
        var templateId = performanceCycleRepository
                .findByPerformanceCycleId(cycleId, TemplateIdOnly.class)
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
                .createdBy(template.getCreatedBy().getId())
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

    public void createFeedbackRequest(Integer requestorId, Integer requestReceiverId, Integer CycleId,
                                      List<Integer> feedbackReceiverIds)
    {

    }
}
