package com.hrms.careerpathmanagement.repositories;

import com.hrms.global.models.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TemplateRepository extends JpaRepository<Template, Integer>, JpaSpecificationExecutor<Template> {
}
