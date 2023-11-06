package com.hrms.careerpathmanagement.services.impl;

import com.hrms.careerpathmanagement.models.PositionLevelPath;
import com.hrms.careerpathmanagement.repositories.PositionLevelPathRepository;
import com.hrms.careerpathmanagement.services.CareerPathManagementService;
import com.hrms.employeemanagement.models.PositionLevel;
import com.hrms.employeemanagement.repositories.PositionLevelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CareerPathManagementServiceImpl implements CareerPathManagementService {
    @Autowired
    PositionLevelPathRepository positionLevelPathRepository;

    @Autowired
    PositionLevelRepository positionLevelRepository;

    @Override
    public List<PositionLevel> getNextPositionLevels(Integer currentPositionLevelId) {
        Specification<PositionLevelPath> currentPosLvSpec = hasCurrentPosLvSpec(currentPositionLevelId);
        findNextsPositionLevel(currentPositionLevelId);
        return positionLevelPathRepository.findAll(currentPosLvSpec)
                .stream().map(PositionLevelPath::getNext).toList();
    }

    public void findNextsPositionLevel(Integer positionLevelId) {
        log.info(String.valueOf(positionLevelRepository.findById(positionLevelId).get().getTitle()));
        var children = positionLevelPathRepository.findAllByCurrentId(positionLevelId)
                .stream().map(PositionLevelPath::getNext).toList();
        children.forEach(c -> findNextsPositionLevel(c.getId()));
    }


    private Specification<PositionLevelPath> hasCurrentPosLvSpec(Integer posLvId) {
        return ((root, query, cb) -> cb.equal(root.get("current").get("id"), posLvId));
    }

    private Specification<PositionLevelPath> hasNextPosLvSpec(Integer posLvId) {
        return ((root, query, cb) -> cb.equal(root.get("next").get("id"), posLvId));
    }
}
