package com.hrms.careerpathmanagement.controllers;

import com.hrms.careerpathmanagement.repositories.PositionLevelPathRepository;
import com.hrms.careerpathmanagement.services.CareerPathManagementService;
import com.hrms.employeemanagement.models.PositionLevel;
import com.hrms.employeemanagement.repositories.PositionLevelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Stack;

@Controller
@Slf4j
public class CareerController {
    @Autowired
    CareerPathManagementService careerPathManagementService;
    @Autowired
    PositionLevelRepository positionLevelRepository;
    @Autowired
    PositionLevelPathRepository positionLevelPathRepository;

    @QueryMapping(name = "getNextPositionLevel")
    public List<PositionLevel> getNextsPositionLevels(@Argument Integer currentPositionLevelId) {
        Stack<PositionLevel> stack = new Stack<>();
        stack.add(positionLevelRepository.findById(currentPositionLevelId).get());
        while (!stack.isEmpty()) {
            var top = stack.pop();
            log.info(top.getTitle());
            var children = positionLevelPathRepository.findAllByCurrentId(top.getId());
            children.forEach(c -> stack.push(positionLevelRepository.findById(c.getNext().getId()).get()));
        }
        return careerPathManagementService.getNextPositionLevels(currentPositionLevelId);
    }

}
