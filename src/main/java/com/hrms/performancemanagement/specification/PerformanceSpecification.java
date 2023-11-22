package com.hrms.performancemanagement.specification;

import com.hrms.performancemanagement.model.PerformanceEvaluation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PerformanceSpecification {
    public <T> Specification<T> hasCycleId(Integer performanceCycleId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("performanceCycle").get("performanceCycleId"), performanceCycleId);
    }

    public <T> Specification<T> hasEmployeeId(Integer employeeId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("employee").get("id"), employeeId);
    }

    public <T> Specification<T> hasEmployeeIds(List<Integer> employeeIds) {
        return (root, query, criteriaBuilder) -> root.get("employee").get("id").in(employeeIds);
    }

    public <T> Specification<T> hasPerformanceCycleId(Integer performCycleId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("performanceCycle").get("performanceCycleId"), performCycleId);
    }

    public <T> Specification<T> hasPerformanceCycleIds(List<Integer> performCycleIds) {
        return (root, query, criteriaBuilder) -> root.get("performanceCycle").get("performanceCycleId").in(performCycleIds);
    }

    public Specification<PerformanceEvaluation> hasJobLevelId(int id) {
        return (root, query, cb) -> cb.equal(root.get("employee").get("position").get("jobLevel").get("id"), id);
    }
}
