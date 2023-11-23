package com.hrms.careerpathmanagement.projection;

public interface GoalProjection {
    Integer getId();
    String getTitle();
    Float getProgress();
    CompetencyCycleSummary getCompetencyCycle();
    interface CompetencyCycleSummary {
        String getCompetencyCycleName();
    }
}