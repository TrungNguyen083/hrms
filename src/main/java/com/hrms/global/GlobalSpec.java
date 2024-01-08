package com.hrms.global;

import com.hrms.employeemanagement.models.EmployeeDamInfo;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class GlobalSpec {
    public static <T> Specification<T> hasId(Integer id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }

    public static <T> Specification<T> hasIds(List<Integer> ids) {
        return (root, query, cb) -> root.get("id").in(ids);
    }

    public static <T> Specification<T> hasPositionId(Integer positionId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("position").get("id"), positionId);
    }

    public static <T> Specification<T> hasPositionIds(List<Integer> positionIds) {
        return (root, query, cb) -> root.get("position").get("id").in(positionIds);
    }

    public static <T> Specification<T> hasStatusTrue() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("status"));
    }

    public static <T> Specification<T> hasJobLevelId(Integer jobLevelId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("jobLevel").get("id"), jobLevelId);
    }

    public static <T> Specification<T> hasDepartmentId(Integer departmentId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("department").get("id"), departmentId);
    }

    public static <T> Specification<T> hasDepartmentIds(List<Integer> departmentIds) {
        return (root, query, criteriaBuilder) -> root.get("department").get("id").in(departmentIds);
    }

    public static <T> Specification<T> hasCompCycleId(Integer cycleId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("competencyCycle").get("id"), cycleId);
    }

    public static <T> Specification<T> hasCompCycleIds(List<Integer> cycleIds) {
        return (root, query, cb) -> root.get("competencyCycle").get("id").in(cycleIds);
    }

    public static Specification<EmployeeDamInfo> hasEmployeeAndType(Integer id, String type) {
        return  (root, query, builder) -> builder.and(
                builder.equal(root.get("employee").get("id"), id),
                builder.equal(root.get("type"), type)
        );
    }

    public static <T> Specification<T> hasEmployeeId(Integer employeeId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("employee").get("id"), employeeId);
    }

    public static <T> Specification<T> hasEmployeeIds(List<Integer> employeeId) {
        return (root, query, criteriaBuilder) -> root.get("employee").get("id").in(employeeId);
    }

    public static <T> Specification<T> hasEmployeeJobLevelId(Integer levelId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("employee").get("jobLevel").get("id"), levelId);
    }

    public static <T> Specification<T> hasEmployeePositionId(Integer positionId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("employee").get("position").get("id"), positionId);
    }

    public static <T> Specification<T> hasEmployeeDepartmentId(Integer departmentId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("employee").get("department").get("id"), departmentId);
    }

    public static <T> Specification<T> hasPerformCycleId(Integer performanceCycleId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("performanceCycle").get("performanceCycleId"), performanceCycleId);
    }

    public static <T> Specification<T> hasPerformCycleIds(List<Integer> performCycleIds) {
        return (root, query, criteriaBuilder) -> root.get("performanceCycle").get("performanceCycleId").in(performCycleIds);
    }

    public static <T> Specification<T> hasSelfScoreExists() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("selfScore"));
    }

    public static <T> Specification<T> hasEvaluatorScoreExists() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("evaluatorScore"));
    }

    public static <T> Specification<T> hasFinalScoreExists() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("finalScore"));
    }

    public static <T> Specification<T> hasFinalEvaluationNotNull() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("finalEvaluation"));
    }

    private GlobalSpec() {
    }

}
