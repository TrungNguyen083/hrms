package com.hrms.careerpathmanagement.dto;

import com.hrms.global.models.Competency;
import com.hrms.global.models.CompetencyGroup;

import java.util.List;

public record CompetencyMatrixTree(CompetencyGroup data, List<Competency> children) {
}
