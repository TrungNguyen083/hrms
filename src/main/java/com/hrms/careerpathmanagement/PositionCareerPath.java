package com.hrms.careerpathmanagement;

public enum PositionCareerPath {
    SOFTWARE_ENGINEER(1, "Software Engineer"),
    QUALITY_ASSURANCE_ENGINEER(2, "Quality Assurance Engineer"),
    ;

    public final int rootPositionLevelId;
    public final String title;

    PositionCareerPath(int rootPositionLevelId, String title) {
        this.rootPositionLevelId = rootPositionLevelId;
        this.title = title;
    }
}