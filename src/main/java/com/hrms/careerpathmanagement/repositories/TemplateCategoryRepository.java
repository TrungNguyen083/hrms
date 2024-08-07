package com.hrms.careerpathmanagement.repositories;

import com.hrms.global.models.TemplateCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TemplateCategoryRepository extends JpaRepository<TemplateCategory, Integer>, JpaSpecificationExecutor<TemplateCategory> {
    List<TemplateCategory> findAllByTemplateId(Integer templateCategoryId);
}
