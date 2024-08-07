//package com.hrms.careerpathmanagement.services;
//
//import com.hrms.careerpathmanagement.dto.CompetencyGroupDTO;
//import com.hrms.careerpathmanagement.dto.RadarChartDTO;
//import com.hrms.careerpathmanagement.dto.TimeLine;
//import com.hrms.careerpathmanagement.dto.TreeSimpleData;
//import com.hrms.careerpathmanagement.input.*;
//import com.hrms.global.models.CompetencyCycle;
//import com.hrms.careerpathmanagement.models.CompetencyEvaluationOverall;
//import com.hrms.global.models.CompetencyTimeLine;
//import com.hrms.careerpathmanagement.models.SkillEvaluation;
//import com.hrms.careerpathmanagement.repositories.*;
//import com.hrms.careerpathmanagement.services.impl.CompetencyServiceImpl;
//import com.hrms.employeemanagement.dto.pagination.EmployeeRatingPagination;
//import com.hrms.global.GlobalSpec;
//import com.hrms.global.dto.BarChartDTO;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import java.text.ParseException;
//import java.util.Collections;
//import java.util.List;
//
//@SpringJUnitConfig
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class CompetencyServiceTest {
//    @Autowired
//    private CompetencyServiceImpl competencyService;
//
//    @Autowired
//    private CompetencyCycleRepository compCycleRepo;
//
//    @Autowired
//    private CompetencyTimeLineRepository compTimeLineRepo;
//
//    @Autowired
//    private QuestionRepository questionRepo;
//
//    @Autowired
//    private CategoryRepository categoryRepo;
//
//    @Autowired
//    private TemplateRepository templateRepo;
//
//    @Autowired
//    private SkillEvaluationRepository ssEvalRepo;
//
//    @Autowired
//    private CompetencyEvaluationOverallRepository ovrRepo;
//
//    @Test
//    void testPostConstruct() {
//        Assertions.assertNotNull(competencyService.getLatestPerformCycle());
//        Assertions.assertNotNull(competencyService.getLatestPerformCycle());
//    }
//
////    @Test
////    void updateIsDoneForOverdueItems_shouldItUpdate() {
////        Assertions.assertTrue(competencyService.updateIsDoneForOverdueItems());
////    }
//
//    @Test
//    void getCompetencyTimeline_shouldItReturn() {
//        Assertions.assertNotNull(competencyService.getCompetencyTimeline(8));
//    }
//
//    @Test
//    void getDepartmentInCompleteComp_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getDepartmentInCompleteComp(8).getDatasets().isEmpty());
//    }
//
//    @Test
//    void getCompetencyEvalProgress_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getCompetencyEvalProgress(8).getLabels().isEmpty());
//        Assertions.assertFalse(competencyService.getCompetencyEvalProgress(8).getDatasets().isEmpty());
//    }
//
//    @Test
//    void getHeatmapCompetency_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getHeatmapCompetency(1, 8).isEmpty());
//        Assertions.assertFalse(competencyService.getHeatmapCompetency(3, 8).isEmpty());
//    }
//
//    @Test
//    void getTopSkillSet_byAllEmployee_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getTopSkill(null, null, 8, 1, 10)
//                .getData().isEmpty());
//        Assertions.assertNotNull(competencyService.getTopSkill(null, null, 8, 1, 10)
//                .getPagination());
//    }
//
//    @Test
//    void getTopSkillSet_byDepartment_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getTopSkill(1, null, 6, 1, 10)
//                .getData().isEmpty());
//        Assertions.assertNotNull(competencyService.getTopSkill(1, null, 6, 1, 10)
//                .getPagination());
//        Assertions.assertFalse(competencyService.getTopSkill(2, null, 6, 1, 10)
//                .getData().isEmpty());
//        Assertions.assertNotNull(competencyService.getTopSkill(2, null, 6, 1, 10)
//                .getPagination());
//    }
//
//    @Test
//    void getTopSkillSet_byEmployee_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getTopSkill(null, 1, 8, 1, 10)
//                .getData().isEmpty());
//        Assertions.assertNotNull(competencyService.getTopSkill(null, 1, 8, 1, 10)
//                .getPagination());
//        Assertions.assertFalse(competencyService.getTopSkill(null, 4, 7, 1, 10)
//                .getData().isEmpty());
//        Assertions.assertNotNull(competencyService.getTopSkill(null, 4, 7, 1, 10)
//                .getPagination());
//        Assertions.assertFalse(competencyService.getTopSkill(null, 5, 7, 1, 10)
//                .getData().isEmpty());
//        Assertions.assertNotNull(competencyService.getTopSkill(null, 5, 7, 1, 10)
//                .getPagination());
//    }
//
//    @Test
//    void getTopKeenSkillSetEmployee_byEmployee_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getTopKeenSkillEmployee(5, 1, 10)
//                .getData().isEmpty());
//        Assertions.assertNotNull(competencyService.getTopKeenSkillEmployee(5, 1, 10)
//                .getPagination());
//    }
//
//    @Test
//    void getTopSkillSetTargetEmployee_byEmployee_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getTopSkillTargetEmployee(5, 1, 10)
//                .getData().isEmpty());
//        Assertions.assertNotNull(competencyService.getTopSkillTargetEmployee(5, 1, 10)
//                .getPagination());
//    }
//
//    @Test
//    void getCurrentEvaluation_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getCurrentEvaluation(5).isEmpty());
//    }
//
//    @Test
//    void getHistoryEvaluations_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getHistoryEvaluations(1).isEmpty());
//        Assertions.assertFalse(competencyService.getHistoryEvaluations(4).isEmpty());
//        Assertions.assertFalse(competencyService.getHistoryEvaluations(5).isEmpty());
//    }
//
//    @Test
//    void getCompanyCompetencyDiffPercent_shouldItHasValue() {
//        Assertions.assertNotNull(competencyService.getCompanyCompetencyDiffPercent(8));
//    }
//
//    @Test
//    void getCompanyCompetencyDiffPercent_shouldItIncrease() {
//        Assertions.assertTrue(competencyService.getCompanyCompetencyDiffPercent(8).getIsIncreased());
//    }
//
//    @Test
//    void getCompetencyChart_shouldItHasValue() {
//        BarChartDTO barChartDTO = competencyService.getCompetencyChart(7);
//
//        Assertions.assertFalse(barChartDTO.getTitle().isEmpty());
//        Assertions.assertFalse(barChartDTO.getItems().isEmpty());
//    }
//
//    @Test
//    void getEmployeeSkillMatrix_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getEmployeeSkillMatrix(4).isEmpty());
//        Assertions.assertTrue(competencyService.getEmployeeSkillMatrix(9).isEmpty());
//    }
//
//    @Test
//    void getSkillMatrixOverall_shouldItHasValue() {
//        Assertions.assertNotNull(competencyService.getSkillMatrixOverall(5));
//        Assertions.assertNull(competencyService.getSkillMatrixOverall(9));
//    }
//
//    @Test
//    void getCompetencyRadarChart_shouldItHasValue() {
//        RadarChartDTO r1 = competencyService.getCompetencyRadarChart(List.of(6, 7, 8), 1);
//        Assertions.assertNotNull(r1);
//        Assertions.assertNotNull(r1.getLabels());
//        Assertions.assertNotNull(r1.getDatasets());
//
//        RadarChartDTO r2 = competencyService.getCompetencyRadarChart(List.of(3, 4), 2);
//        Assertions.assertNull(r2);
//    }
//
//    @Test
//    void getCompetencyRating_byDepartment_shouldItHasValue() {
//        EmployeeRatingPagination p1 = competencyService.getCompetencyRating(1, 7, 1, 10);
//        Assertions.assertNotNull(p1);
//        Assertions.assertNotNull(p1.data());
//        Assertions.assertNotNull(p1.pagination());
//
//        EmployeeRatingPagination p2 = competencyService.getCompetencyRating(2, 3, 1, 10);
//        Assertions.assertTrue(p2.data().isEmpty());
//    }
//
//    @Test
//    void getCompetencyRating_byEmployee_shouldItHasValue() {
//        EmployeeRatingPagination p1 = competencyService.getCompetencyRating(null, 7, 1, 10);
//        Assertions.assertNotNull(p1);
//        Assertions.assertNotNull(p1.data());
//        Assertions.assertNotNull(p1.pagination());
//
//        EmployeeRatingPagination p2 = competencyService.getCompetencyRating(null, 3, 1, 10);
//        Assertions.assertTrue(p2.data().isEmpty());
//    }
//
//    @Test
//    void getDepartmentSkillSetHeatmap_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getDepartmentSkillHeatmap(1, 7, List.of(5, 9), List.of(1, 2, 3, 4, 5, 6)).isEmpty());
//        Assertions.assertTrue(competencyService.getDepartmentSkillHeatmap(2, 7, List.of(4, 19, 42), List.of(1, 2, 3, 4, 5, 6)).get(0).getScore() != 0);
//        Assertions.assertTrue(competencyService.getDepartmentSkillHeatmap(3, 7, List.of(8), List.of(1, 2, 3, 4, 5, 6)).get(0).getScore() == 0);
//    }
//
//    @Test
//    void getDepartmentCompetencyGap_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getDepartmentCompetencyGap(7, List.of(5, 9)).getDatasets().isEmpty());
//        Assertions.assertTrue(competencyService.getDepartmentCompetencyGap(7, List.of(19, 42)).getDatasets().get(0).getDataset().get(0) == 0);
//    }
//
//    @Test
//    void getEvaluationCycles_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getEvaluationCycles().isEmpty());
//    }
//
//    @Test
//    void createCompetencyCycle_shouldItCanCreate() {
//        CompetencyCycleInput input = new CompetencyCycleInput("New Cycle", "New Cycle Description", "2021-01-01", "2021-12-31", "DEPARTMENT_MANAGER");
//        CompetencyCycle competencyCycle = competencyService.createCompetencyCycle(input);
//
//        Assertions.assertNotNull(competencyCycle);
//
//        CompetencyCycle find = compCycleRepo.findById(competencyCycle.getId()).orElse(null);
//
//        Assertions.assertNotNull(find);
//        Assertions.assertEquals(input.getCycleName(), find.getCompetencyCycleName());
//        Assertions.assertEquals(input.getDescription(), find.getDescription());
//        Assertions.assertNotNull(find.getStartDate().toString());
//        Assertions.assertNotNull(find.getDueDate().toString());
//        Assertions.assertEquals(input.getEvaluatorType(), find.getEvaluatorType());
//        Assertions.assertNull(find.getInitialDate());
//    }
//
//    @Test
//    void createCompetencyProcess_shouldItCanCreate() throws ParseException {
//        List<TimeLineInput> tli = List.of(
//                new TimeLineInput("TimeLine 1", "2021-01-01", "2021-01-31"),
//                new TimeLineInput("TimeLine 2", "2021-02-01", "2021-02-28"),
//                new TimeLineInput("TimeLine 3", "2021-03-01", "2021-03-31"),
//                new TimeLineInput("TimeLine 4", "2021-04-01", "2021-04-30"));
//        EvaluationProcessInput input = new EvaluationProcessInput(tli, "2021-01-01T17:00:00.000Z", 7);
//
//        List<TimeLine> listTL = competencyService.createCompetencyProcess(input);
//
//        Assertions.assertNotNull(listTL);
//
//        CompetencyCycle find = compCycleRepo.findById(7).orElse(null);
//
//        Assertions.assertNotNull(find);
//        Assertions.assertNotNull(find.getInitialDate());
//
//        List<CompetencyTimeLine> listCC = compTimeLineRepo.findAll(GlobalSpec.hasCompCycleId(7));
//
//        Assertions.assertEquals(4, listCC.size());
//    }
//
//    @Test
//    void getTemplates_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getTemplates().isEmpty());
//    }
//
//    @Test
//    void createTemplate_shouldItCanCreate() {
//        long templateCount = templateRepo.count();
//        long categoryCount = categoryRepo.count();
//        long questionCount = questionRepo.count();
//
//        List<CategoryInput> lci = List.of(
//                new CategoryInput("Category 1", "Category 1 Description", 1F, List.of(
//                        new QuestionInput("Question 1", "Question 1 Description", 2F),
//                        new QuestionInput("Question 2", "Question 2 Description", 2F))),
//                new CategoryInput("Category 2", "Category 2 Description", 2F, List.of(
//                        new QuestionInput("Question 3", "Question 3 Description", 2F),
//                        new QuestionInput("Question 4", "Question 4 Description", 2F))));
//        TemplateInput input = new TemplateInput("New Template", "New Template Description", 1, lci);
//
//        Assertions.assertTrue(competencyService.createTemplate(input));
//        Assertions.assertEquals(templateCount + 1, templateRepo.count());
//        Assertions.assertEquals(categoryCount + 2, categoryRepo.count());
//        Assertions.assertEquals(questionCount + 4, questionRepo.count());
//    }
//
//    @Test
//    void getTrackEvaluationProgress_shouldItHasValue() {
//        Assertions.assertFalse(competencyService.getTrackEvaluationProgress(8, 1, 10).getData().isEmpty());
//        Assertions.assertNotNull(competencyService.getTrackEvaluationProgress(8, 1, 10).getPagination());
//        Assertions.assertTrue(competencyService.getTrackEvaluationProgress(9, 1, 10).getData().isEmpty());
//        Assertions.assertNotNull(competencyService.getTrackEvaluationProgress(9, 1, 10).getPagination());
//    }
//
//    @Test
//    void getEvaluateSkillSetForm_shouldItHasValue() {
//        List<TreeSimpleData> l1 = competencyService.getEvaluateSkillForm(4);
//        List<TreeSimpleData> l2 = competencyService.getEvaluateSkillForm(5);
//        List<TreeSimpleData> l3 = competencyService.getEvaluateSkillForm(6);
//        Assertions.assertNotNull(l1);
//        Assertions.assertNotNull(l2);
//        Assertions.assertNotNull(l3);
//        Assertions.assertFalse(l1.get(0).getChildren().isEmpty());
//        Assertions.assertFalse(l2.get(0).getChildren().isEmpty());
//        Assertions.assertTrue(l3.get(0).getChildren().isEmpty());
//    }
//
//    @Test
//    void getCompetencyGroups_shouldItHasValue() {
//        List<CompetencyGroupDTO> c = competencyService.getCompetencyGroups();
//
//        Assertions.assertNotNull(c);
//        Assertions.assertFalse(c.isEmpty());
//    }
//
//    @Test
//    void getEvaluationResult_success() {
//        Assertions.assertNotNull(competencyService.getEvaluationResult(4, 7));
//    }
//
//    @Test
//    void getEvaluationResult_fail_cycleIdWrong() {
//        Assertions.assertEquals(Collections.emptyList(), competencyService.getEvaluationResult(4, 10));
//    }
//
//    @Test
//    void getEvaluationResult_fail_employeeIdWrong() {
//        Assertions.assertEquals(Collections.emptyList(), competencyService.getEvaluationResult(9, 7));
//    }
//
//    @Test
//    void createSelfCompetencyEvaluation_saveDraft_shouldItCanCreate() {
//        List<SkillScoreInput> lss = List.of(
//                new SkillScoreInput(1, 2),
//                new SkillScoreInput(2, 2),
//                new SkillScoreInput(3, 2),
//                new SkillScoreInput(4, 2),
//                new SkillScoreInput(5, 3),
//                new SkillScoreInput(6, 3),
//                new SkillScoreInput(7, 3),
//                new SkillScoreInput(8, 3));
//        CompetencyEvaluationInput cei = new CompetencyEvaluationInput(3, 8, false, null, lss);
//        Assertions.assertTrue(competencyService.createSelfCompetencyEvaluation(cei));
//
//        Specification<SkillEvaluation> spec = GlobalSpec.hasCompCycleId(8);
//        spec = spec.and(GlobalSpec.hasEmployeeId(3));
//        List<SkillEvaluation> listCT = ssEvalRepo.findAll(spec);
//
//        Assertions.assertNotNull(listCT);
//        Assertions.assertEquals(8, listCT.size());
//    }
//
//    @Test
//    void createSelfCompetencyEvaluation_submit_shouldItCanCreate() {
//        List<SkillScoreInput> lss = List.of(
//                new SkillScoreInput(1, 2),
//                new SkillScoreInput(2, 2),
//                new SkillScoreInput(3, 2),
//                new SkillScoreInput(4, 2),
//                new SkillScoreInput(5, 3),
//                new SkillScoreInput(6, 3),
//                new SkillScoreInput(7, 3),
//                new SkillScoreInput(8, 3));
//        CompetencyEvaluationInput cei = new CompetencyEvaluationInput(3, 8, true, null, lss);
//        Assertions.assertTrue(competencyService.createSelfCompetencyEvaluation(cei));
//
//        Specification<SkillEvaluation> spec = GlobalSpec.hasCompCycleId(8);
//        spec = spec.and(GlobalSpec.hasEmployeeId(3));
//        List<SkillEvaluation> listCT = ssEvalRepo.findAll(spec);
//
//        Assertions.assertNotNull(listCT);
//        Assertions.assertEquals(8, listCT.size());
//
//        Specification<CompetencyEvaluationOverall> spec1 = GlobalSpec.hasCompCycleId(8);
//        spec1 = spec1.and(GlobalSpec.hasEmployeeId(3));
//        CompetencyEvaluationOverall ovr = ovrRepo.findAll(spec1).get(0);
//
//        Assertions.assertEquals(true, ovr.getIsSelfSubmitted());
//        Assertions.assertEquals("Completed", ovr.getEmployeeStatus());
//    }
//
//    @Test
//    void createEvaluatorCompetencyEvaluation_saveDraft_shouldItCanCreate() {
//        List<SkillScoreInput> lss = List.of(
//                new SkillScoreInput(1, 2),
//                new SkillScoreInput(2, 2),
//                new SkillScoreInput(3, 2),
//                new SkillScoreInput(4, 2),
//                new SkillScoreInput(5, 3),
//                new SkillScoreInput(6, 3),
//                new SkillScoreInput(7, 3),
//                new SkillScoreInput(8, 3));
//        CompetencyEvaluationInput cei = new CompetencyEvaluationInput(3, 8, false, null, lss);
//        Assertions.assertTrue(competencyService.createEvaluatorCompetencyEvaluation(cei));
//
//        Specification<SkillEvaluation> spec = GlobalSpec.hasCompCycleId(8);
//        spec = spec.and(GlobalSpec.hasEmployeeId(3));
//        List<SkillEvaluation> listCT = ssEvalRepo.findAll(spec);
//
//        Assertions.assertNotNull(listCT);
//        Assertions.assertEquals(8, listCT.size());
//    }
//
//    @Test
//    void createEvaluatorCompetencyEvaluation_submit_shouldItCanCreate() {
//        List<SkillScoreInput> lss = List.of(
//                new SkillScoreInput(1, 2),
//                new SkillScoreInput(2, 2),
//                new SkillScoreInput(3, 2),
//                new SkillScoreInput(4, 2),
//                new SkillScoreInput(5, 3),
//                new SkillScoreInput(6, 3),
//                new SkillScoreInput(7, 3),
//                new SkillScoreInput(8, 3));
//        CompetencyEvaluationInput cei = new CompetencyEvaluationInput(3, 8, true, null, lss);
//        Assertions.assertTrue(competencyService.createEvaluatorCompetencyEvaluation(cei));
//
//        Specification<SkillEvaluation> spec = GlobalSpec.hasCompCycleId(8);
//        spec = spec.and(GlobalSpec.hasEmployeeId(3));
//        List<SkillEvaluation> listCT = ssEvalRepo.findAll(spec);
//
//        Assertions.assertNotNull(listCT);
//        Assertions.assertEquals(8, listCT.size());
//
//        Specification<CompetencyEvaluationOverall> spec1 = GlobalSpec.hasCompCycleId(8);
//        spec1 = spec1.and(GlobalSpec.hasEmployeeId(3));
//        CompetencyEvaluationOverall ovr = ovrRepo.findAll(spec1).get(0);
//
//        Assertions.assertEquals(true, ovr.getIsEvaluatorSubmitted());
//        Assertions.assertEquals("Completed", ovr.getEvaluatorStatus());
//    }
//
//    @Test
//    void createFinalCompetencyEvaluation_saveDraft_shouldItCanCreate() {
//        List<SkillScoreInput> lss = List.of(
//                new SkillScoreInput(1, 2),
//                new SkillScoreInput(2, 2),
//                new SkillScoreInput(3, 2),
//                new SkillScoreInput(4, 2),
//                new SkillScoreInput(5, 3),
//                new SkillScoreInput(6, 3),
//                new SkillScoreInput(7, 3),
//                new SkillScoreInput(8, 3));
//        CompetencyEvaluationInput cei = new CompetencyEvaluationInput(3, 8, false, null, lss);
//        Assertions.assertTrue(competencyService.createFinalCompetencyEvaluation(cei));
//
//        Specification<SkillEvaluation> spec = GlobalSpec.hasCompCycleId(8);
//        spec = spec.and(GlobalSpec.hasEmployeeId(3));
//        List<SkillEvaluation> listCT = ssEvalRepo.findAll(spec);
//
//        Assertions.assertNotNull(listCT);
//        Assertions.assertEquals(8, listCT.size());
//    }
//
//    @Test
//    void createFinalCompetencyEvaluation_submit_shouldItCanCreate() {
//        List<SkillScoreInput> lss = List.of(
//                new SkillScoreInput(1, 2),
//                new SkillScoreInput(2, 2),
//                new SkillScoreInput(3, 2),
//                new SkillScoreInput(4, 2),
//                new SkillScoreInput(5, 3),
//                new SkillScoreInput(6, 3),
//                new SkillScoreInput(7, 3),
//                new SkillScoreInput(8, 3));
//        CompetencyEvaluationInput cei = new CompetencyEvaluationInput(3, 8, true, null, lss);
//        Assertions.assertTrue(competencyService.createFinalCompetencyEvaluation(cei));
//
//        Specification<SkillEvaluation> spec = GlobalSpec.hasCompCycleId(8);
//        spec = spec.and(GlobalSpec.hasEmployeeId(3));
//        List<SkillEvaluation> listCT = ssEvalRepo.findAll(spec);
//
//        Assertions.assertNotNull(listCT);
//        Assertions.assertEquals(8, listCT.size());
//
//        Specification<CompetencyEvaluationOverall> spec1 = GlobalSpec.hasCompCycleId(8);
//        spec1 = spec1.and(GlobalSpec.hasEmployeeId(3));
//        CompetencyEvaluationOverall ovr = ovrRepo.findAll(spec1).get(0);
//
//        Assertions.assertEquals(true, ovr.getIsFinalSubmitted());
//        Assertions.assertEquals("Completed", ovr.getFinalStatus());
//    }
//
//    @Test
//    void createCompetencyEvaluation_selfEvaluatorFinalWithSameRow() {
//        long skillSetEvaluationCount = ssEvalRepo.count();
//
//        List<SkillScoreInput> lss = List.of(
//                new SkillScoreInput(1, 2),
//                new SkillScoreInput(2, 2),
//                new SkillScoreInput(3, 2),
//                new SkillScoreInput(4, 2),
//                new SkillScoreInput(5, 3),
//                new SkillScoreInput(6, 3),
//                new SkillScoreInput(7, 3),
//                new SkillScoreInput(8, 3));
//        CompetencyEvaluationInput cei = new CompetencyEvaluationInput(3, 8, true, null, lss);
//        Assertions.assertTrue(competencyService.createSelfCompetencyEvaluation(cei));
//        Assertions.assertTrue(competencyService.createEvaluatorCompetencyEvaluation(cei));
//        Assertions.assertTrue(competencyService.createFinalCompetencyEvaluation(cei));
//
//        Assertions.assertEquals(skillSetEvaluationCount + 8, ssEvalRepo.count());
//    }
//}
