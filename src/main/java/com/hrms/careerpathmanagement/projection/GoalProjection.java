package com.hrms.careerpathmanagement.projection;

public interface GoalProjection {
    Integer getId();
    String getTitle();
    Float getProgress();
    CompetencyCycleSummary getCompetencyCycle();

    interface CompetencyCycleSummary {
        Integer getId();
        String getCompetencyCycleName();
    }
}