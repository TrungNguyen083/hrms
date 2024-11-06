package com.hrms.performancemanagement.repositories;

import com.hrms.global.models.EvaluateCycle;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EvaluateCycleRepository extends JpaRepository<EvaluateCycle, Integer>, JpaSpecificationExecutor<EvaluateCycle> {
    EvaluateCycle findFirstByStatusNotOrderByStartDateDesc(String status);

    EvaluateCycle findByYear(Integer year);

    @Modifying
    @Transactional
    @Query("UPDATE EvaluateCycle ctl SET ctl.status = 'In Progress' WHERE ctl.initialDate < current_date AND ctl.status ='Not Start'")
    void activeNewEvaluationCycle();
}
