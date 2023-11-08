package com.hrms.employeemanagement.services;

import com.hrms.employeemanagement.documents.EmployeeDocument;
import com.hrms.employeemanagement.dto.*;
import com.hrms.employeemanagement.models.Employee;
import com.hrms.global.paging.PagingInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeManagementService {
	List<Employee> getAllEmployees();
	Employee createEmployee(EmployeeInputDTO input) throws Exception;
	Employee findEmployee(Integer id);
	Employee updateEmployee(EmployeeInputDTO input);
	EmployeeDetailDTO getEmployeeDetail(Integer id);
	List<Employee> findEmployees(List<Integer> departmentIds);
	List<Employee> findEmployees(Integer departmentId);
	List<Employee> getNewEmployees();
	EmployeePagingDTO filterEmployees(List<Integer> departmentIds,
									  List<Integer> currentContracts,
									  Boolean status,
									  String name,
									  PagingInfo pagingInfo);
	HeadcountDTO getHeadcountsStatistic();
	List<HeadcountChartDataDTO> getHeadcountChartData();
	void uploadFile(MultipartFile file, Integer employeeId, String type) throws IOException;
	String getQualifications(Integer employeeId);
	List<EmployeeDocument> searchEmployees(String name);
}
