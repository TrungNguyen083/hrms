package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.Goal;
import com.hrms.careerpathmanagement.projection.GoalProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Integer>, JpaSpecificationExecutor<Goal> {

    Page<GoalProjection> findAllByEmployeeId(Integer employeeId, Pageable page);

    default long countByDepartmentCycleStatus(Integer departmentId, Integer cycleId, String status) {
        return countByEmployeeDepartmentIdAndCompetencyCycleIdAndStatusIs(departmentId, cycleId, status);
    }
    long countByEmployeeDepartmentIdAndCompetencyCycleIdAndStatusIs(Integer departmentId,
                                                                    Integer cycleId,
                                                                    String status);

    default List<Goal> findAllByDepartmentAndCycle(Integer departmentId, Integer cycleId) {
        return findAllByEmployeeDepartmentIdAndCompetencyCycleId(departmentId, cycleId);
    }

    List<Goal> findAllByEmployeeDepartmentIdAndCompetencyCycleId(Integer departmentId, Integer cycleId);

    List<Goal> findAllByEmployeeDepartmentIdAndCompetencyCycleIdOrderByUpdatedAt(Integer departmentId,
                                                                           Integer cycleId,
                                                                           Pageable pageable);
}
