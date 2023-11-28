package com.hrms.global.mapper;

import com.hrms.careerpathmanagement.dto.TimeLine;
import com.hrms.careerpathmanagement.input.TimeLineInput;
import com.hrms.careerpathmanagement.models.CompetencyTimeLine;
import com.hrms.careerpathmanagement.models.ProficiencyLevel;
import com.hrms.performancemanagement.input.PerformanceRangeInput;
import com.hrms.performancemanagement.input.ProficiencyLevelInput;
import com.hrms.performancemanagement.model.PerformanceTimeLine;
import com.hrms.usermanagement.dto.UserDto;
import com.hrms.usermanagement.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

        this.typeMap(TimeLineInput.class, CompetencyTimeLine.class).addMappings(mapper -> {
            mapper.map(TimeLineInput::getTimeLineName, CompetencyTimeLine::setCompetencyTimeLineName);
            mapper.map(TimeLineInput::getStartDate, CompetencyTimeLine::setStartDate);
            mapper.map(TimeLineInput::getDueDate, CompetencyTimeLine::setDueDate);
        });

        this.typeMap(TimeLineInput.class, PerformanceTimeLine.class).addMappings(mapper -> {
            mapper.map(TimeLineInput::getTimeLineName, PerformanceTimeLine::setPerformanceTimeLineName);
            mapper.map(TimeLineInput::getStartDate, PerformanceTimeLine::setStartDate);
            mapper.map(TimeLineInput::getDueDate, PerformanceTimeLine::setDueDate);
        });
    }
}