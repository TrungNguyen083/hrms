package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.TemplateCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TemplateCategoryRepository extends JpaRepository<TemplateCategory, Integer>, JpaSpecificationExecutor<TemplateCategory> {
}
