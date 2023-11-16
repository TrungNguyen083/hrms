package com.hrms.search.configuration;

import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SearchConfig {
    public static class Employee {
        public static final String INDEX = "employee";
        public static final List<String> FIELDS = List.of("full_name");
    }

    public static class SkillSet {
        public static final String INDEX = "skill_set";
        public static final List<String> FIELDS = List.of("skill_set_name");
    }

    public static class Competency {
        public static final String INDEX = "competency";
        public static final List<String> FIELDS = List.of("competency_name", "description");
    }

    public static class CompetencyCycle {
        public static final String INDEX = "competency_cycle";
        public static final List<String> FIELDS = List.of("competency_cycle_name", "description");
    }

    public static class PerformanceCycle {
        public static final String INDEX = "performance_cycle";
        public static final List<String> FIELDS = List.of("performance_cycle_name", "description");
    }

    public static class Position {
        public static final String INDEX = "position";
        public static final List<String> FIELDS = List.of("position_name");
    }
}
