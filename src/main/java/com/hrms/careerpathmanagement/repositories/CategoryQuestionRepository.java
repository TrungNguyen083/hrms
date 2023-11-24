package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.CategoryQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryQuestionRepository extends JpaRepository<CategoryQuestion, Integer>, JpaSpecificationExecutor<CategoryQuestion> {
}
