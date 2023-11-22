package com.hrms.employeemanagement.services;

import com.hrms.careerpathmanagement.dto.DiffPercentDTO;
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
	List<Employee> getAllEmployeesHaveDepartment();
	Employee createEmployee(EmployeeInputDTO input) throws Exception;
	Employee findEmployee(Integer id);
	Employee updateEmployee(EmployeeInputDTO input);
	EmployeeDetailDTO getEmployeeDetail(Integer id);
	List<Employee> getNewEmployees();
	EmployeePagingDTO filterEmployees(List<Integer> departmentIds,
                                      List<Integer> currentContracts,
                                      Boolean status,
                                      String name,
                                      PagingInfo pagingInfo);
	DiffPercentDTO getHeadcountsStatistic();
	BarChartDTO getHeadcountChartData();
	void uploadPersonalFile(MultipartFile file, Integer employeeId, String type) throws IOException;
	String getProfilePicture(Integer employeeId);

	List<EmployeeDamInfoDTO> getQualifications(Integer employeeId);

    EmployeeOverviewDTO getProfileOverview(Integer employeeId);

	List<EmployeeItemDTO> getDepartmentEmployees(Integer departmentId, Integer positionId);

    List<ProfileImageOnly> getEmployeesNameAndAvatar(List<Integer> ids);
}
