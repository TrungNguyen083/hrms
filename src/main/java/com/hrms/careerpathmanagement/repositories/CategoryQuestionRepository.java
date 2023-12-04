package com.hrms.careerpathmanagement.repositories;

import com.hrms.careerpathmanagement.models.CategoryQuestion;
import com.hrms.careerpathmanagement.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CategoryQuestionRepository extends JpaRepository<CategoryQuestion, Integer>, JpaSpecificationExecutor<CategoryQuestion> {
    List<CategoryQuestion> findAllByCategoryId(Integer categoryId);

    @Query("select cq from CategoryQuestion cq where cq.category.id in :categoryIds")
    List<CategoryQuestion> findAllByCategoryIdIn(List<Integer> categoryIds);

}
