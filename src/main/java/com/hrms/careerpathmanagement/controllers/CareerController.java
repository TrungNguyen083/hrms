package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.dto.PositionLevelNodeDTO;
import com.hrms.careerpathmanagement.services.CareerManagementService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class CareerController {
    @Autowired
    private CareerManagementService careerManagementService;

    @GetMapping("/career/{positionLevelId}")
    @ResponseBody
    public PositionLevelNodeDTO getCareerPathFrom(@PathVariable Integer positionLevelId) {
        return careerManagementService.getCareerPathFrom(positionLevelId);
    }

    @GetMapping("/career/{employeeId}/{positionId}/{levelId}")
    @ResponseBody
    public Float getMatchPercent(@PathVariable Integer employeeId,
                                 @PathVariable Integer positionId,
                                 @PathVariable Integer levelId) {
        return careerManagementService.getMatchPercent(employeeId, positionId, levelId);
    }
}
