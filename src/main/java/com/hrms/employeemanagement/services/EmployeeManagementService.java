package com.hrms.employeemanagement.services;

import com.hrms.careerpathmanagement.dto.PercentageChangeDTO;
import com.hrms.employeemanagement.dto.*;
import com.hrms.employeemanagement.dto.pagination.EmployeePagingDTO;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.employeemanagement.projection.ProfileImageOnly;
import com.hrms.global.dto.BarChartDTO;
import com.hrms.global.paging.PagingInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeManagementService {
	List<Employee> getAllEmployees();
	List<Employee> getAllEmployeesEvaluate();
	List<Employee> getAllEmployeesHaveDepartment();
	Employee createEmployee(EmployeeInputDTO input);
	Employee findEmployee(Integer id);
	Employee updateEmployee(EmployeeInputDTO input);
	EmployeeDTO getEmployeeDetail(Integer id);
	List<EmployeeDTO> getNewEmployees();
	EmployeePagingDTO filterEmployees(List<Integer> departmentIds,
                                      List<Integer> currentContracts,
                                      Boolean status,
                                      String name,
                                      Integer pageNo, Integer pageSize);
	PercentageChangeDTO getHeadcountsStatistic(Integer cycleId);
	BarChartDTO getHeadcountChartData();
	void uploadPersonalFile(MultipartFile file, Integer employeeId, String type, String title) throws IOException;
	String getProfilePicture(Integer employeeId);

	List<QualificationDTO> getQualifications(Integer employeeId);

    EmployeeOverviewDTO getProfileOverview(Integer employeeId);

    List<ProfileImageOnly> getEmployeesNameAndAvatar(List<Integer> ids);

    List<Employee> getEmployeesInDepartment(Integer departmentId);

	List<NameImageDTO> getNameImagesInDepartment(Integer departmentId);

	PercentageChangeDTO getDepartmentHeadcount(Integer cycleId, Integer departmentId);

	BarChartDTO getDepartmentHeadcountChart(Integer departmentId);

	Integer getEmployeeIdByEmail(String email);

	String getProfileImageByEmail(String email);

	Integer getDepartmentIdByEmail(String email);
}
