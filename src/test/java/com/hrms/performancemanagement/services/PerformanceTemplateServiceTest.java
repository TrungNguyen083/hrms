package com.hrms.performancemanagement.services;

import com.hrms.careerpathmanagement.models.*;
import com.hrms.careerpathmanagement.repositories.*;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.performancemanagement.model.PerformanceCycle;
import com.hrms.performancemanagement.projection.TemplateIdOnly;
import com.hrms.performancemanagement.repositories.FeedbackRequestRepository;
import com.hrms.performancemanagement.repositories.PerformanceCycleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PerformanceTemplateServiceTest {
    @Autowired
    private CategoryQuestionRepository categoryQuestionRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private TemplateCategoryRepository templateCategoryRepository;
    @Autowired
    private PerformanceEvaluationRepository performanceEvaluationRepository;
    @Autowired
    private PerformanceCycleRepository performanceCycleRepository;
    @Autowired
    private FeedbackRequestRepository feedbackRequestRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    private PerformanceTemplateService performanceTemplateService;

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
            .withExposedPorts(3306)
            .withDatabaseName("hrms")
            .withUsername("root")
            .withPassword("root");

    @BeforeEach
    void init() {
        performanceTemplateService = new PerformanceTemplateService(categoryQuestionRepository,
                questionRepository,
                categoryRepository,
                templateRepository,
                templateCategoryRepository,
                performanceEvaluationRepository,
                performanceCycleRepository,
                feedbackRequestRepository);

        Employee e1 = new Employee();
        e1.setId(1);
        e1.setInsertionTime(new Date());
        e1.setModificationTime(new Date());

        Employee e2 = new Employee();
        e2.setId(2);
        e2.setInsertionTime(new Date());
        e2.setModificationTime(new Date());

        Employee e3 = new Employee();
        e3.setId(3);
        e3.setInsertionTime(new Date());
        e3.setModificationTime(new Date());

        employeeRepository.saveAll(List.of(e1, e2, e3));
    }

    @Test
    void contextLoads() {
        assert mysql.isRunning() == true;
        assert performanceTemplateService != null;
    }

    @Test
    void givenCycleId_whenGetTemplateOfCycle_thenException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            performanceTemplateService.getTemplateOfCycle(1);
        });
        String expectedMessage = "Template not found";
        String actualMessage = exception.getMessage();

        assert (actualMessage.contains(expectedMessage));
    }

    @Test
    void givenCycleId_whenGetTemplateOfCycle_thenTemplate() {
        Employee e1 = new Employee();
        e1.setId(1);

        Template template = new Template();
        template.setId(1);
        template.setTemplateName("Template 1");
        template.setCreatedAt(new Date());
        template.setCreatedBy(e1);

        PerformanceCycle pc = new PerformanceCycle();
        pc.setPerformanceCycleId(1);
        pc.setTemplate(template);
        pc.setInsertionTime(new Date());
        pc.setModificationTime(new Date());

        templateRepository.save(template);
        performanceCycleRepository.save(pc);

        Category category = new Category();
        category.setId(1);
        category.setCategoryName("Category 1");

        Question question = new Question();
        question.setId(1);
        question.setQuestionName("Question 1");

        CategoryQuestion categoryQuestion = new CategoryQuestion();
        categoryQuestion.setCategory(category);
        categoryQuestion.setQuestion(question);

        TemplateCategory templateCategory = new TemplateCategory();
        templateCategory.setTemplate(template);
        templateCategory.setCategory(category);

        categoryRepository.save(category);
        questionRepository.save(question);
        categoryQuestionRepository.save(categoryQuestion);
        templateCategoryRepository.save(templateCategory);

        var evalTemplate = performanceTemplateService.getTemplateAndQuestion(1);
        assert (evalTemplate.getTemplate().getTemplateName().equals(template.getTemplateName()));
    }

    @Test
    void givenCycle_whenGetTemplate_ReturnEvalTemplateDto() {
        Template template = new Template();
        template.setId(1);
        template.setTemplateName("Template 1");
        template.setCreatedAt(new Date());

        PerformanceCycle pc = new PerformanceCycle();
        pc.setPerformanceCycleId(1);
        pc.setTemplate(template);
        pc.setInsertionTime(new Date());
        pc.setModificationTime(new Date());

        templateRepository.save(template);
        performanceCycleRepository.save(pc);

        var evalTemplate = performanceTemplateService.getTemplateOfCycle(1);
        assert(evalTemplate.getTemplateName().equals(template.getTemplateName()));
    }

    @Test
    void createFeedbackRequest_shouldReturnSuccessMsg() {
        var msg = performanceTemplateService.createFeedbackRequest(1,
                List.of(2),
                1,
                3,
                "Test");

        assert (msg.equals("Feedback request created"));
    }

    @Test
    void getFeedbacks() {
    }
}