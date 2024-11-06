package com.hrms.global.mapper;

import com.hrms.global.input.EvaluateCycleInput;
import com.hrms.global.input.TimeLineInput;
import com.hrms.global.models.EvaluateCycle;
import com.hrms.global.models.EvaluateTimeLine;
import com.hrms.global.models.ProficiencyLevel;
import com.hrms.performancemanagement.input.PerformanceRangeInput;
import com.hrms.careerpathmanagement.input.ProficiencyLevelInput;
import com.hrms.usermanagement.dto.UserDto;
import com.hrms.usermanagement.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class HrmsMapper extends ModelMapper {
    @Bean
    void setup() {
        this.typeMap(User.class, UserDto.class).addMappings(
                mapper -> {
                    mapper.map(User::getUserId, UserDto::setUserId);
                    mapper.map(User::getUsername, UserDto::setUserName);
                    mapper.map(User::getIsEnabled, UserDto::setStatus);
                    mapper.map(User::getCreatedAt, UserDto::setCreatedAt);
                }
        );
        this.typeMap(PerformanceRangeInput.class, PerformanceRangeInput.class).addMappings(
                mapper -> {
                    mapper.map(PerformanceRangeInput::getMinValue, PerformanceRangeInput::setMinValue);
                    mapper.map(PerformanceRangeInput::getMaxValue, PerformanceRangeInput::setMaxValue);
                    mapper.map(PerformanceRangeInput::getText, PerformanceRangeInput::setText);
                }
        );
        this.typeMap(ProficiencyLevelInput.class, ProficiencyLevel.class).addMappings(mapper -> {
            mapper.skip(ProficiencyLevel::setId);
            mapper.map(ProficiencyLevelInput::getName, ProficiencyLevel::setProficiencyLevelName);
            mapper.map(ProficiencyLevelInput::getDescription, ProficiencyLevel::setProficiencyLevelDescription);
            mapper.map(ProficiencyLevelInput::getScore, ProficiencyLevel::setScore);
        });

        this.typeMap(EvaluateCycleInput.class, EvaluateCycle.class).addMappings(mapper -> {
            mapper.map(EvaluateCycleInput::getEndDate, EvaluateCycle::setDueDate);
        });

        this.typeMap(TimeLineInput.class, EvaluateTimeLine.class).addMappings(mapper -> {
            mapper.map(TimeLineInput::getTimeLineName, EvaluateTimeLine::setEvaluateTimeLineName);
            mapper.map(TimeLineInput::getEndDate, EvaluateTimeLine::setDueDate);
        });
    }
}