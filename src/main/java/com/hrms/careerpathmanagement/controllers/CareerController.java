package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.PositionCareerPath;
import com.hrms.careerpathmanagement.dto.CareerPathTreeDTO;
import com.hrms.careerpathmanagement.services.CareerManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/careers")
@Slf4j
public class CareerController {
    private final CareerManagementService careerManagementService;

    @Autowired
    public CareerController(CareerManagementService careerManagementService) {
        this.careerManagementService = careerManagementService;
    }

//    @GetMapping("/path")
//    public ResponseEntity<CareerPathTreeDTO> getCareerPathTree(@RequestParam PositionCareerPath position) {
//        return ResponseEntity.ok(careerManagementService.getCareerPathTree(position));
//    }

    @GetMapping("/match")
    public ResponseEntity<Float> getMatchPercent(@RequestParam Integer employeeId,
                                 @RequestParam Integer positionId,
                                 @RequestParam Integer levelId) {
        return ResponseEntity.ok(careerManagementService.getMatchPercent(employeeId, positionId, levelId));
    }
}
