package com.hrms.employeemanagement.repositories;

import com.hrms.employeemanagement.models.EmployeeDamInfo;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeDamInfoRepository extends JpaRepository<EmployeeDamInfo, Integer>, JpaSpecificationExecutor<EmployeeDamInfo> {

    /***
     * Get the latest profile image of employees
     * @param fileType type of profile image, e.g: PROFILE_IMAGE, QUALIFICATION, DEGREE, ...
     * @return
     */
    @Query("SELECT new com.hrms.employeemanagement.projection.ProfileImageOnly(edi.employee.id, edi.url) " +
            "FROM EmployeeDamInfo edi " +
            "WHERE edi.employee.id IN :idsSet " +
            "    AND edi.uploadedAt = ( " +
            "        SELECT MAX(edi2.uploadedAt) " +
            "        FROM EmployeeDamInfo edi2 " +
            "        WHERE edi2.employee.id = edi.employee.id " +
            "    ) " +
            "    AND edi.type = :fileType")
    List<ProfileImageOnly> findByEmployeeIdsSetAndFileType(List<Integer> idsSet, String fileType);

    //<T> Collection<T> findByEmployee
}
