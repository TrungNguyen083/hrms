package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.dto.CompareOverview;
import com.hrms.careerpathmanagement.dto.pagination.PromotionPaging;
import com.hrms.careerpathmanagement.services.CareerService;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CareerController {
    private final CareerService careerService;

    @Autowired
    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @GetMapping("/match")
    public ResponseEntity<Float> getMatchPercent(@RequestParam Integer employeeId,
                                 @RequestParam Integer positionId,
                                 @RequestParam Integer levelId) {
        return ResponseEntity.ok(careerService.getMatchPercent(employeeId, positionId, levelId));
    }

    @QueryMapping(name = "compareOverviews")
    @PreAuthorize("hasAuthority('HR')")
    public List<CompareOverview> getCompareOverview(@Argument List<Integer> employeeIds, @Argument Integer cycleId) {
        return careerService.getCompareOverview(employeeIds, cycleId);
    }



    @MutationMapping(name = "requestPromotion")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean createRequestPromotion(@Argument List<Integer> employeeIds, @Argument Integer cycleId) {
        return careerService.createRequestPromotion(employeeIds, cycleId);
    }

    @QueryMapping(name = "promotionList")
    @PreAuthorize("hasAuthority('HR')")
    public PromotionPaging getPromotionList(@Argument Integer cycleId, @Nullable @Argument String name,
                                            @Argument Integer pageNo, @Argument Integer pageSize) {
        return careerService.getPromotionList(cycleId, name, pageNo, pageSize);
    }

    @MutationMapping(name = "updatePromotionRequest")
    @PreAuthorize("hasAuthority('HR')")
    public Boolean updatePromotionRequest(@Argument Integer employeeId, @Argument Integer cycleId,
                                          @Argument Boolean isApprove, @Argument String comment) {
        return careerService.updatePromotionRequest(employeeId, cycleId, isApprove, comment);
    }
}
