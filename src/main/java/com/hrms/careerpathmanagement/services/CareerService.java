package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.CompareOverview;
import com.hrms.careerpathmanagement.dto.pagination.PromotionPaging;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CareerService {
    Float getMatchPercent(Integer employeeId, Integer positionId, Integer levelId);

    Boolean createRequestPromotion(List<Integer> employeeIds, Integer cycleId);

    PromotionPaging getPromotionList(Integer cycleId, String name, Integer pageNo, Integer pageSize);

    List<CompareOverview> getCompareOverview(List<Integer> employeeIds, Integer cycleId);
}
