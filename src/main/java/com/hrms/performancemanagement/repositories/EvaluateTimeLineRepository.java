package com.hrms.performancemanagement.repositories;

import com.hrms.global.models.EvaluateTimeLine;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluateTimeLineRepository extends JpaRepository<EvaluateTimeLine, Integer>, JpaSpecificationExecutor<EvaluateTimeLine> {
    //check if current date is after due date set isDone to true
    @Modifying
    @Transactional
    @Query("UPDATE EvaluateTimeLine ctl SET ctl.isDone = true WHERE ctl.dueDate < current_date")
    void updateIsDoneForOverdueItems();
}
