package com.hrms.careerpathmanagement.services;

import com.hrms.careerpathmanagement.dto.PositionLevelNodeDTO;
import com.hrms.careerpathmanagement.repositories.PositionLevelPathRepository;
import com.hrms.employeemanagement.repositories.PositionLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class CareerManagementService {
    private PositionLevelPathRepository positionLevelPathRepository;

    private PositionLevelRepository positionLevelRepository;

    @Autowired
    public CareerManagementService(PositionLevelPathRepository positionLevelPathRepository,
                                   PositionLevelRepository positionLevelRepository) {
        this.positionLevelPathRepository = positionLevelPathRepository;
        this.positionLevelRepository = positionLevelRepository;
    }

    public PositionLevelNodeDTO getCareerPathFrom(Integer positionLevelId) {
        var title = positionLevelRepository.findById(positionLevelId).get().getTitle();


        if (!positionLevelPathRepository.existsByCurrentId(positionLevelId))
            return new PositionLevelNodeDTO(positionLevelId, title, null);


        var node = new PositionLevelNodeDTO(positionLevelId, title, new LinkedList<>());

        var nextPositionLevels = positionLevelPathRepository.findAllByCurrentId(positionLevelId)
                .stream().map(i -> i.getNext())
                .toList();

        nextPositionLevels.forEach(i -> node.getNextPositionLevels().add(getCareerPathFrom(i.getId())));

        return node;
    }
}
