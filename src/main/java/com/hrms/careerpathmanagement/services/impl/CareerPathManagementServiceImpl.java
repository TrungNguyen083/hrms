package com.hrms.careerpathmanagement.services.impl;

import com.hrms.careerpathmanagement.dto.PositionLevelNodeDTO;
import com.hrms.careerpathmanagement.models.PositionLevelPath;
import com.hrms.careerpathmanagement.repositories.PositionLevelPathRepository;
import com.hrms.careerpathmanagement.services.CareerPathManagementService;
import com.hrms.employeemanagement.models.PositionLevel;
import com.hrms.employeemanagement.repositories.EmployeeRepository;
import com.hrms.employeemanagement.repositories.PositionLevelRepository;
import com.hrms.employeemanagement.specification.EmployeeSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CareerPathManagementServiceImpl implements CareerPathManagementService {
    PositionLevelPathRepository positionLevelPathRepository;
    PositionLevelRepository positionLevelRepository;
    EmployeeRepository employeeRepository;
    EmployeeSpecification employeeSpecification;

    @Autowired
    public CareerPathManagementServiceImpl(PositionLevelPathRepository positionLevelPathRepository, PositionLevelRepository positionLevelRepository, EmployeeRepository employeeRepository, EmployeeSpecification employeeSpecification) {
        this.positionLevelPathRepository = positionLevelPathRepository;
        this.positionLevelRepository = positionLevelRepository;
        this.employeeRepository = employeeRepository;
        this.employeeSpecification = employeeSpecification;
    }

    @Override
    public List<PositionLevel> getNextPositionLevel(Integer currentPositionLevelId) {
        Specification<PositionLevelPath> currentPosLvSpec = hasCurrentPosLvSpec(currentPositionLevelId);
        getCareerPathTree(currentPositionLevelId);
        return positionLevelPathRepository.findAll(currentPosLvSpec)
                .stream().map(PositionLevelPath::getNext).toList();
    }

    @Override
    public PositionLevelNodeDTO getCareerPath(Integer employeeId) {
        var emp = employeeRepository.findById(employeeId);
        var posLv = positionLevelRepository.findByPositionIdAndJobLevelId(emp.get().getPosition().getId(),
                emp.get().getJobLevel().getId());

        return getCareerPathTree(posLv.getId());
    }

    private PositionLevelNodeDTO getCareerPathTree(Integer positionLevelId) {
        log.info("getCareerPathTree: " + positionLevelId);
        var node = new PositionLevelNodeDTO();
        node.setTitle(positionLevelRepository.findById(positionLevelId).get().getTitle());
        node.setMatchPercentage(0f);
        node.setNextPositionLevels(List.of());

        positionLevelPathRepository.findAllByCurrentId(positionLevelId)
                .stream().map(PositionLevelPath::getNext).toList()
                .forEach(item -> node.getNextPositionLevels().add(getCareerPathTree(item.getId())));

        return node;
    }

    /**
     * Get match percentage of employee with position level
     * @param employeeId
     * @param positionLevelId
     * @return match percentage
     */
    private Float getMatchPercentage(Integer employeeId, Integer positionLevelId) {
        var emp = employeeRepository.findById(employeeId);
        var posLv = positionLevelRepository.findByPositionIdAndJobLevelId(emp.get().getPosition().getId(),
                emp.get().getJobLevel().getId());

        return 0f;
    }

    private Specification<PositionLevelPath> hasCurrentPosLvSpec(Integer posLvId) {
        return ((root, query, cb) -> cb.equal(root.get("current").get("id"), posLvId));
    }
}
